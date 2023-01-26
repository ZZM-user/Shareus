<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryRef" :inline="true" :model="queryParams" label-width="68px">
      <el-form-item label="发送人" prop="senderName">
        <el-input
            v-model="queryParams.senderName"
            clearable
            placeholder="请输入发送人昵称"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送人" prop="senderId">
        <el-input
            v-model="queryParams.senderId"
            clearable
            placeholder="请输入发送人QQ"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="抽取内容" prop="extract">
        <el-input
            v-model="queryParams.extract"
            clearable
            placeholder="请输入抽取内容"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="求文状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择求文状态">
          <el-option
              v-for="dict in mirai_qiuwen_log_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="反馈者" prop="answerId">
        <el-input
            v-model="queryParams.answerId"
            clearable
            placeholder="请输入求文反馈者QQ"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送时间" prop="sendTime">
        <el-date-picker
            v-model="queryParams.sendTime"
            placeholder="请选择发送时间"
            type="datetime"
        />
      </el-form-item>
      <el-form-item label="反馈时间" prop="finishTime">
        <el-date-picker
            v-model="queryParams.finishTime"
            placeholder="请选择反馈时间"
            type="datetime"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['mirai:qiuwen_log:add']"
            icon="Plus"
            plain
            type="primary"
            @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['mirai:qiuwen_log:edit']"
            :disabled="single"
            icon="Edit"
            plain
            type="success"
            @click="handleUpdate"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['mirai:qiuwen_log:remove']"
            :disabled="multiple"
            icon="Delete"
            plain
            type="danger"
            @click="handleDelete"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['mirai:qiuwen_log:export']"
            icon="Download"
            plain
            type="warning"
            @click="handleExport"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="qiuwen_logList" :highlight-current-row="true" :max-height="1000" :stripe="true"
              @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="id" prop="id"/>
      <el-table-column align="center" label="发送人昵称" prop="senderName" width="120px"/>
      <el-table-column align="center" label="发送人QQ" prop="senderId" width="120px"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="发送内容" prop="content" width="120px"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="抽取内容" prop="extract"
                       width="180"/>
      <el-table-column align="center" label="求文状态" prop="status">
        <template #default="scope">
          <dict-tag :options="mirai_qiuwen_log_status" :value="scope.row.status"
                    style="cursor: pointer"
                    @click="handleChangeQiuwenStatus(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="求文时间" prop="sendTime" width="160"/>
      <el-table-column align="center" label="反馈者" prop="answerId" width="120px">
        <template #default="scope">
          <div>
            <span v-if="scope.row.answerId===0">机器人</span>
            <span v-else>{{ scope.row.answerId }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="反馈结果" prop="result"/>
      <el-table-column align="center" label="反馈时间" prop="finishTime" width="160"/>
      <el-table-column align="center" class-name="small-padding fixed-width" fixed="right" label="操作">
        <template #default="scope">
          <el-button v-hasPermi="['mirai:qiuwen_log:edit']" icon="Edit" link type="primary"
                     @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button v-hasPermi="['mirai:qiuwen_log:remove']" icon="Delete" link type="danger"
                     @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        v-model:limit="queryParams.pageSize"
        v-model:page="queryParams.pageNum"
        :total="total"
        @pagination="getList"
    />

    <!-- 添加或修改求文日志对话框 -->
    <el-dialog v-model="open" :title="title" append-to-body width="650px">
      <el-form ref="qiuwen_logRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="昵称" prop="senderName">
          <el-input v-model="form.senderName" placeholder="请输入发送人昵称"/>
        </el-form-item>
        <el-form-item label="QQ" prop="senderId">
          <el-input v-model="form.senderId" placeholder="请输入发送人QQ"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" autosize placeholder="请输入内容" type="textarea"/>
        </el-form-item>
        <el-form-item label="抽取内容" prop="extract">
          <el-input v-model="form.extract" placeholder="请输入抽取内容"/>
        </el-form-item>
        <el-form-item label="求文状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in mirai_qiuwen_log_status"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="反馈者" prop="answerId">
          <el-input v-model="form.answerId" placeholder="请输入求文反馈者(0 机器人/其他人QQ)"/>
        </el-form-item>
        <el-form-item label="反馈结果" prop="result">
          <el-input v-model="form.result" placeholder="请输入内容" type="textarea"/>
        </el-form-item>
        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker
              v-model="form.sendTime"
              placeholder="请选择发送时间"
              type="datetime"
          />
        </el-form-item>
        <el-form-item label="反馈时间" prop="finishTime">
          <el-date-picker
              v-model="form.finishTime"
              placeholder="请选择反馈时间"
              type="datetime"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 修改日志状态 -->
    <el-dialog v-model="openChangeStatus" append-to-body title="修改求文状态" width="550px">
      <el-form ref="qiuwen_logRef" :disabled="disOpenChangeStatus" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="求文状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in mirai_qiuwen_log_status"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.status==2" label="反馈结果" prop="result">
          <el-col v-if="disOpenChangeStatus">{{ form.result }}</el-col>
          <el-input v-else v-model="form.result" :rows="3" autosize clearable placeholder="请输入反馈结果"
                    type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :disabled="disOpenChangeStatus" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancelChangeQiuwenStatus">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script name="Qiuwen_log" setup>
import {addQiuwen_log, delQiuwen_log, getQiuwen_log, listQiuwen_log, updateQiuwen_log} from "@/api/mirai/qiuwen_log";

const {proxy} = getCurrentInstance();
const {mirai_qiuwen_log_status} = proxy.useDict('mirai_qiuwen_log_status');

const qiuwen_logList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const openChangeStatus = ref(false);
const disOpenChangeStatus = ref(true);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    senderName: null,
    senderId: null,
    content: null,
    extract: null,
    status: null,
    answerId: null,
    result: null,
    sendTime: null,
    finishTime: null
  },
  rules: {
    status: [{required: true, message: "求文状态不能为空", trigger: "blur"}]
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询求文日志列表 */
function getList() {
  loading.value = true;
  listQiuwen_log(queryParams.value).then(response => {
    qiuwen_logList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    senderName: null,
    senderId: null,
    content: null,
    extract: null,
    status: null,
    answerId: null,
    result: null,
    sendTime: null,
    finishTime: null
  };
  proxy.resetForm("qiuwen_logRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加求文日志";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value
  getQiuwen_log(id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改求文日志";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["qiuwen_logRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateQiuwen_log(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          openChangeStatus.value = false;
          getList();
        });
      } else {
        addQiuwen_log(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || ids.value;
  proxy.$confirm('是否确认删除求文日志编号为"' + ids + '"的数据项？'
  ).then(function () {
    return delQiuwen_log(ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/**切换状态*/
function handleChangeQiuwenStatus(row) {
  disOpenChangeStatus.value = true;
  reset();
  const id = row.id || ids.value
  getQiuwen_log(id).then(response => {
    form.value = response.data;
    openChangeStatus.value = true;
    if (form.value.status == 1) {
      disOpenChangeStatus.value = false
    }
  });
}

/** 取消切换求文状态*/
function cancelChangeQiuwenStatus() {
  openChangeStatus.value = false;
  reset();
}


/** 导出按钮操作 */
function handleExport() {
  proxy.download('bot/qiuwen_log/export', {
    ...queryParams.value
  }, `求文日志数据导出_${new Date().getTime()}.xlsx`)
}

getList();
</script>
