<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryRef" :inline="true" :model="queryParams" label-width="68px">
      <el-form-item label="QQ号码" prop="qq">
        <el-input
            v-model="queryParams.qq"
            clearable
            placeholder="请输入QQ号码"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickName">
        <el-input
            v-model="queryParams.nickName"
            clearable
            placeholder="请输入昵称"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="头像" prop="avatrarUrl">
        <el-input
            v-model="queryParams.avatrarUrl"
            clearable
            placeholder="请输入头像"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="特殊头衔" prop="specialTitle">
        <el-input
            v-model="queryParams.specialTitle"
            clearable
            placeholder="请输入特殊头衔"
            @keyup.enter="handleQuery"
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
            v-hasPermi="['system:member:add']"
            icon="Plus"
            plain
            type="primary"
            @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            v-hasPermi="['system:member:edit']"
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
            v-hasPermi="['system:member:remove']"
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
            v-hasPermi="['system:member:export']"
            icon="Download"
            plain
            type="warning"
            @click="handleExport"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="头像" prop="avatarUrl"/>
      <el-table-column align="center" label="QQ号码" prop="qq"/>
      <el-table-column align="center" label="昵称" prop="nickName"/>
      <el-table-column align="center" label="特殊头衔" prop="specialTitle"/>
      <el-table-column align="center" label="备注" prop="remark"/>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template #default="scope">
          <el-button v-hasPermi="['system:member:edit']" icon="Edit" link type="primary"
                     @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button v-hasPermi="['system:member:remove']" icon="Delete" link type="primary"
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

    <!-- 添加或修改QQ成员对话框 -->
    <el-dialog v-model="open" :title="title" append-to-body width="500px">
      <el-form ref="memberRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="QQ号码" prop="qq">
          <el-input v-model="form.qq" placeholder="请输入QQ号码"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="头像" prop="avatrarUrl">
          <el-input v-model="form.avatrarUrl" placeholder="请输入头像"/>
        </el-form-item>
        <el-form-item label="特殊头衔" prop="specialTitle">
          <el-input v-model="form.specialTitle" placeholder="请输入特殊头衔"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
        </el-form-item>
        <el-form-item label="删除标记 0未删；1删" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标记 0未删；1删"/>
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

<script name="Member" setup>
import {delGroup} from "../../../api/mirai/group";
import {addMember, getMember, listMember, updateMember} from "../../../api/mirai/member";
import {toRefs} from "vue";

const {proxy} = getCurrentInstance();

const memberList = ref([]);
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
    qq: null,
    nickName: null,
    avatrarUrl: null,
    specialTitle: null,
  },
  rules: {
    qq: [
      {
        required: true, message: "QQ号码不能为空", trigger: "blur"
      }
    ],
    nickName: [
      {
        required: true, message: "昵称不能为空", trigger: "blur"
      }
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询QQ成员列表 */
function getList() {
  loading.value = true;
  listMember(queryParams.value).then(response => {
    memberList.value = response.rows;
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
    qq: null,
    nickName: null,
    avatrarUrl: null,
    specialTitle: null,
    remark: null,
    delFlag: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  };
  proxy.resetForm("memberRef");
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
  title.value = "添加QQ成员";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getMember(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改QQ成员";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["memberRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateMember(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addMember(form.value).then(response => {
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
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除QQ成员编号为"' + _ids + '"的数据项？').then(function () {
    return delGroup(ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}


/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/member/export', {
    ...queryParams.value
  }, `member_${new Date().getTime()}.xlsx`)
}

getList();
</script>
