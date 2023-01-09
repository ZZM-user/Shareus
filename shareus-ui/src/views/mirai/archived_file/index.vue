<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryRef" :inline="true" :model="queryParams" label-width="68px">
      <el-form-item label="文件名" prop="name">
        <el-input
            v-model="queryParams.name"
            clearable
            placeholder="请输入文件名"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发件人" prop="senderId">
        <el-input
            v-model="queryParams.senderId"
            clearable
            placeholder="请输入发件人QQ"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="MD5" prop="md5">
        <el-input
            v-model="queryParams.md5"
            clearable
            placeholder="请输入MD5"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否展示" prop="enabled">
        <el-select v-model="queryParams.enabled" clearable placeholder="请选择是否展示">
          <el-option
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="源路径" prop="originUrl">
        <el-input
            v-model="queryParams.originUrl"
            clearable
            placeholder="请输入源路径"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="归档路径" prop="archiveUrl">
        <el-input
            v-model="queryParams.archiveUrl"
            clearable
            placeholder="请输入归档路径"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="归档日期" prop="archiveDate">
        <el-date-picker
            v-model="queryParams.archiveDate"
            placeholder="请选择归档日期"
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
            v-hasPermi="['mirai:archived_file:add']"
            icon="Plus"
            plain
            type="primary"
            @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['mirai:archived_file:edit']"
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
            v-hasPermi="['mirai:archived_file:remove']"
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
            v-hasPermi="['mirai:archived_file:export']"
            icon="Download"
            plain
            type="warning"
            @click="handleExport"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="archived_fileList" :highlight-current-row="true" :max-height="1000"
              :stripe="true"
              @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="id" prop="id"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="文件名" prop="name" width="350"/>
      <el-table-column align="center" label="发件人" prop="senderId" width="120"/>
      <el-table-column align="center" label="大小" prop="size"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="MD5" prop="md5"/>
      <el-table-column align="center" label="是否展示" prop="enabled">
        <template #default="scope">
          <el-switch
              v-model="scope.row.enabled"
              :active-value="0"
              :inactive-value="1"
              class="ml-2"
              style="--el-switch-on-color: #13ce66"
          />
        </template>

      </el-table-column>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="源路径" prop="originUrl"/>
      <el-table-column :show-tooltip-when-overflow="true" align="center" label="存档路径" prop="archiveUrl"/>
      <el-table-column align="center" label="归档日期" prop="archiveDate" width="160"/>
      <el-table-column align="center" class-name="small-padding fixed-width" fixed="right" label="操作">
        <template #default="scope">
          <el-button v-hasPermi="['mirai:archived_file:edit']" icon="Edit" link type="primary"
                     @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button v-hasPermi="['mirai:archived_file:remove']" icon="Delete" link type="danger"
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

    <!-- 添加或修改归档文件对话框 -->
    <el-dialog v-model="open" :title="title" append-to-body width="600px">
      <el-form ref="archived_fileRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名" prop="name">
          <el-input v-model="form.name" placeholder="请输入文件名"/>
        </el-form-item>
        <el-form-item label="发件人" prop="senderId">
          <el-input v-model="form.senderId" placeholder="请输入发件人QQ"/>
        </el-form-item>
        <el-form-item label="大小" prop="size">
          <el-input v-model="form.size" placeholder="请输入大小(KB)"/>
        </el-form-item>
        <el-form-item label="MD5" prop="md5">
          <el-input v-model="form.md5" placeholder="请输入MD5"/>
        </el-form-item>
        <el-form-item label="是否展示" prop="enabled">
          <el-radio-group v-model="form.enabled">
            <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="源路径" prop="originUrl">
          <el-input v-model="form.originUrl" placeholder="请输入源路径"/>
        </el-form-item>
        <el-form-item label="归档路径" prop="archiveUrl">
          <el-input v-model="form.archiveUrl" placeholder="请输入归档路径"/>
        </el-form-item>
        <el-form-item label="归档日期" prop="archiveDate">
          <el-date-picker
              v-model="form.archiveDate"
              placeholder="请选择归档日期"
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
  </div>
</template>

<script name="Archived_file" setup>
import {delArchived_file, getArchived_file, listArchived_file} from "@/api/mirai/archived_file";
import {updateArchived_file} from "../../../api/mirai/archived_file";

const {proxy} = getCurrentInstance();
const {sys_normal_disable} = proxy.useDict('sys_normal_disable');

const archived_fileList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    senderId: null,
    size: null,
    md5: null,
    enabled: null,
    originUrl: null,
    archiveUrl: null,
    archiveDate: null
  },
  rules: {
    name: [
      {
        required: true, message: "文件名不能为空", trigger: "blur"
      }
    ],
    size: [
      {
        required: true, message: "长度不能为空", trigger: "blur"
      }
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询归档文件列表 */
function getList() {
  loading.value = true;
  listArchived_file(queryParams.value).then(response => {
    archived_fileList.value = response.rows;
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
    name: null,
    senderId: null,
    size: null,
    md5: null,
    enabled: null,
    delFlag: null,
    originUrl: null,
    archiveUrl: null,
    archiveDate: null
  };
  proxy.resetForm("archived_fileRef");
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
  title.value = "添加归档文件";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getArchived_file(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改归档文件";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.refs["archived_fileRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateArchived_file(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addArchived_file(form.value).then(response => {
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
  proxy.confirm('是否确认删除归档文件编号为"' + ids + '"的数据项？'
  ).then(function () {
    return delArchived_file(ids
    )
        ;
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}


/** 导出按钮操作 */
function handleExport() {
  proxy.download('mirai/archived_file/export', {
    ...queryParams.value
  }, `archived_file_${new Date().getTime()}.xlsx`)
}

getList();
</script>
