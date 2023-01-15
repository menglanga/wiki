<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-form
                        layout="inline"
                        :model="param"
                >
                    <a-form-item>
                        <a-input v-model:value="param.loginName" placeholder="登录名">
                        </a-input>
                    </a-form-item>
                    <a-form-item>
                        <a-button
                                type="primary"
                                @click="handleQuery({page: 1, size: pagination.pageSize})"
                        >
                            查询
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()">新增</a-button>
                    </a-form-item>
                </a-form>
            </p>
            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="users"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
            >
                <!--<template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar"/>
                </template>-->
                <!--<template v-slot:category="{ text, record }">
                    <span>{{getCategoryName(record.category1Id)}}/{{getCategoryName(record.category2Id)}}</span>
                </template>-->
                <template v-slot:action="{ text, record }">
                    <a-space size="small">
                        <!--<router-link :to="'/admin/doc?userId='+record.id">
                            <a-button type="primary">用户管理</a-button>
                        </router-link>-->
                        <a-button type="primary" @click="edit(record)">编辑</a-button>
                        <a-popconfirm
                                title="删除后不可恢复，确认删除？"
                                ok-text="是"
                                cancel-text="否"
                                @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
    <a-modal
            title="用户表单"
            v-model:visible="modelVisible"
            :confirm-loading="modelLoading"
            @ok="handleModelOk"
    >
        <a-form :model="user" :label-col="{span: 6}">
            <a-form-item label="登录名">
                <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
            </a-form-item>
            <a-form-item label="昵称">
                <a-input v-model:value="user.name"/>
            </a-form-item>
            <a-form-item label="密码">
                <a-input v-model:value="user.password"/>
            </a-form-item>
            <!--<a-form-item label="描述">
                <a-input v-model:value="user.description" type="text"/>
            </a-form-item>-->
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {message} from 'ant-design-vue';
    import {Tool} from "@/util/tool";

    declare let hexMd5: any;
    declare let KEY: any;

    export default defineComponent({
        name: 'AdminUser',
        setup() {
            const param = ref();
            param.value = {};
            const users = ref();
            const pagination = ref({
                current: 1,
                pageSize: 4,
                total: 0
            });
            const loading = ref(false);

            const columns = [

                {
                    title: '登录名',
                    dataIndex: 'loginName'
                },


                {
                    title: '名称',
                    dataIndex: 'name'
                },
                {
                    title: '密码',
                    dataIndex: 'password'
                },

                {
                    title: 'Action',
                    key: 'action',
                    slots: {customRender: 'action'}
                }
            ];

            //  数据查询
            const handleQuery = (params: any) => {
                loading.value = true;
                axios.get("/user/list", {
                    params: {
                        pageNum: params.pageNum,
                        pageSize: params.pageSize,
                        loginName: param.value.loginName
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        users.value = data.data.list;
                        //重置分页按钮
                        pagination.value.current = params.pageNum;
                        pagination.value.total = data.data.total;
                    } else {
                        message.error(data.message);
                    }


                });
            };

            //表格点击页码触发
            const handleTableChange = (pagination: any) => {
                // console.log("看看看自带的分页参数都有啥："+ pagination);
                handleQuery({
                    pageNum: pagination.current,
                    pageSize: pagination.pageSize
                });
            };

            //表单
            //数组【100,101】对应：前端开发/vue
            // const categoryIds = ref();
            const user = ref();
            const modelVisible = ref(false);
            const modelLoading = ref(false);

            const handleModelOk = () => {
                modelLoading.value = true;

                user.value.password=hexMd5(user.value.password+KEY);
                // user.value.category1Id = categoryIds.value[0];
                // user.value.category2Id = categoryIds.value[1];
                axios.post("/user/save", user.value).then((response) => {
                    modelLoading.value = false;
                    const data = response.data;
                    if (data.success) {
                        modelVisible.value = false;

                        //重新加载列表
                        handleQuery({
                            pageNum: pagination.value.current,
                            pageSize: pagination.value.pageSize
                        });
                    } else {
                        message.error(data.message);
                    }
                });
            };


            //编辑
            const edit = (record: any) => {
                modelVisible.value = true;
                user.value = Tool.copy(record);
                // categoryIds.value = [user.value.category1Id, user.value.category2Id]
            };

            //新增
            const add = () => {
                modelVisible.value = true;
                user.value = {};
            };


            //删除
            const handleDelete = (id: any) => {
                axios.delete("/user/delete/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        //重新加载列表
                        handleQuery({
                            pageNum: pagination.value.current,
                            pageSize: pagination.value.pageSize
                        });
                    }
                });
            };

            /**
             * 一级分类树，children属性就是二级分类
             * [{
             *     id: "",
             *     name: "",
             *     children:[{
             *         id: "",
             *         name: ""
             *     }]
             * }]
             */
            const level1 = ref();

            // let categorys: any;

            //  数据查询所有分类
            // const handleQueryCategory = () => {
            //     loading.value = true;
            //     axios.get("/category/all").then((response) => {
            //         loading.value = false;
            //         const data = response.data;
            //         if (data.success) {
            //             categorys = data.data;
            //             console.log("原始数组：", categorys);
            //             level1.value = [];
            //             level1.value = Tool.array2tree(categorys, 0);
            //             console.log("树形结构：", level1.value);
            //             //加载完分类后在加载电子书，否则分类树加载很慢会导致电子书选人出错
            //             handleQuery({
            //                 pageNum: 1,
            //                 pageSize: pagination.value.pageSize
            //             });
            //         } else {
            //             message.error(data.message);
            //         }
            //     });
            // };

            // const getCategoryName = (cid: number) => {
            //     let result = "";
            //     categorys.forEach((item: any) => {
            //         if (item.id === cid) {
            //             result = item.name;
            //         }
            //     });
            //     return result;
            // };

            onMounted(() => {
                // handleQueryCategory();
                handleQuery({
                    pageNum: 1,
                    pageSize: pagination.value.pageSize,
                });

            });

            return {
                // categoryIds,
                level1,
                users,
                param,
                pagination,
                columns,
                loading,
                handleTableChange,
                edit,
                add,
                handleDelete,
                modelVisible,
                modelLoading,
                handleModelOk,
                handleQuery,
                user,
                // getCategoryName
            }
        }
    })
</script>
