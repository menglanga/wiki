<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-row :gutter="24">
                <a-col :span="8">
                    <p>
                        <a-form
                                layout="inline"
                                :model="param"
                        >
                            <a-form-item>
                                <a-button
                                        type="primary"
                                        @click="handleQuery()"
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
                            v-if="level1.length>0"
                            :columns="columns"
                            :row-key="record => record.id"
                            :data-source="level1"
                            :pagination="false"
                            :loading="loading"
                            size="small"
                            :defaultExpandAllRows="true"
                    >
                        <template #name="{ text, record }">
                            {{record.sort}} {{text}}
                        </template>
                        <template v-slot:action="{ text, record }">
                            <a-space size="small">
                                <a-button type="primary" @click="edit(record)" size="small">编辑</a-button>
                                <a-popconfirm
                                        title="删除后不可恢复，确认删除？"
                                        ok-text="是"
                                        cancel-text="否"
                                        @confirm="handleDelete(record.id)"
                                >
                                    <a-button type="danger"  size="small">删除</a-button>
                                </a-popconfirm>
                            </a-space>
                        </template>
                    </a-table>
                </a-col>
                <a-col :span="16">
                    <p>
                        <a-form layout="inline" :model="param">
                            <a-form-item>
                                <a-button type="primary" @click="handleSave()">
                                    保存
                                </a-button>
                            </a-form-item>
                        </a-form>
                    </p>
                    <a-form :model="doc" layout="vertical">
                        <a-form-item label="名称">
                            <a-input v-model:value="doc.name" placehold="请输入文档名"/>
                        </a-form-item>
                        <a-form-item label="父文档">
                            <a-tree-select
                                    v-model:value="doc.parent"
                                    style="width: 100%"
                                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                                    :tree-data="treeSelectData"
                                    placeholder="请选择父文档"
                                    tree-default-expand-all
                                    :replaceFields="{title:'name',key:'id',value: 'id',}"
                            >
                            </a-tree-select>
                        </a-form-item>
                        <a-form-item label="顺序">
                            <a-input v-model:value="doc.sort"  placehold="请输入顺序"/>
                        </a-form-item>
                        <a-form-item label="内容">
                            <div id="content"></div>
                        </a-form-item>
                    </a-form>
                </a-col>
            </a-row>

        </a-layout-content>
    </a-layout>
<!--    <a-modal-->
<!--            title="文档表单"-->
<!--            v-model:visible="modelVisible"-->
<!--            :confirm-loading="modelLoading"-->
<!--            @ok="handleModelOk"-->
<!--    >-->
<!--       -->
<!--    </a-modal>-->
</template>

<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {message} from 'ant-design-vue';
    import {Tool} from "@/util/tool";
    import {useRoute} from "vue-router";
    import E from "wangeditor";

    export default defineComponent({
        name: 'AdminDoc',
        setup() {
            const route = useRoute();
            const param = ref();
            param.value = {};
            const docs = ref();
            const loading = ref(false);

            const columns = [
                {
                    title: '名称',
                    dataIndex: 'name',
                    slots: {customRender: 'name'}
                },
                // {
                //     title: '父文档',
                //     key: 'parent',
                //     dataIndex: 'parent'
                // },
                // {
                //     title: '顺序',
                //     dataIndex: 'sort',
                // },
                {
                    title: 'Action',
                    key: 'action',
                    slots: {customRender: 'action'}
                }
            ];

            /**
             * 一级文档树，children属性就是二级文档
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
            level1.value=[];

            //  数据查询
            const handleQuery = () => {
                loading.value = true;
                axios.get("/doc/all").then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        docs.value = data.data;
                        console.log("原始数组：", docs.value);
                        level1.value = [];
                        level1.value = Tool.array2tree(docs.value, 0);
                        console.log("树形结构：", level1.value);
                    } else {
                        message.error(data.message);
                    }


                });
            };



            //表单
            const treeSelectData = ref();
            treeSelectData.value = [];
            const doc = ref();
            doc.value={};
            const modelVisible = ref(false);
            const modelLoading = ref(false);
            const editor=new E('#content');

            editor.config.zIndex=0;

            const handleSave = () => {
                modelLoading.value = true;
                doc.value.content=editor.txt.html();
                axios.post("/doc/save", doc.value).then((response) => {
                    modelLoading.value = false;
                    const data = response.data;
                    if (data.success) {
                        modelVisible.value = false;

                        //重新加载列表
                        handleQuery();
                    } else {
                        message.error(data.message);
                    }
                });
            };


            const setDisable = (treeSelectData: any, id: any) => {
                for (let i = 0; i < treeSelectData.length; i++) {
                    const node = treeSelectData[i];
                    if (node.id === id) {
                        console.log("disabled", node);
                        node.disabled = true;
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            for (let j = 0; j < children.length; j++) {
                                setDisable(children, children[j].id);
                            }
                        }
                    } else {
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            setDisable(children,id);
                        }
                    }
                }
            };

            const ids:Array<string>=[];

            const getDeleteIds = (treeSelectData: any, id: any) => {
                for (let i = 0; i < treeSelectData.length; i++) {
                    const node = treeSelectData[i];
                    if (node.id === id) {
                        console.log("disabled", node);
                        // node.disabled = true;
                        ids.push(id);
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            for (let j = 0; j < children.length; j++) {
                                getDeleteIds(children, children[j].id);
                            }
                        }
                    } else {
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            getDeleteIds(children,id);
                        }
                    }
                }
            };


            //  富文本内容查询
            const handleQueryContent = () => {
                loading.value = true;
                axios.get("/doc/find-content/"+doc.value.id).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        editor.txt.html(data.data)
                    } else {
                        message.error(data.message);
                    }


                });
            };


            //编辑
            const edit = (record: any) => {
                modelVisible.value = true;
                doc.value = Tool.copy(record);
                handleQueryContent();
                //不能选择当前节点节气所有子孙节点作为父节点
                treeSelectData.value = Tool.copy(level1.value);
                setDisable(treeSelectData.value, record.id);

                //为选择树添加一个无
                treeSelectData.value.unshift({id: 0, name: '无'});
                /*setTimeout(function () {
                    editor.create();
                },100);*/
            };

            //新增
            const add = () => {
                modelVisible.value = true;
                treeSelectData.value = Tool.copy(level1.value);
                treeSelectData.value.unshift({id: 0, name: '无'});

                doc.value = {
                    ebookId: route.query.ebookId
                };

                /*setTimeout(function () {
                    editor.create();
                },100);*/
            };


            //删除
            const handleDelete = (id: any) => {
                getDeleteIds(level1.value,id);
                axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        //重新加载列表
                        handleQuery();
                    }
                });
            };

            onMounted(() => {
                handleQuery();
                editor.create();
            });

            return {
                level1,
                param,
                columns,
                loading,
                edit,
                add,
                handleDelete,
                modelVisible,
                modelLoading,
                handleSave,
                handleQuery,
                doc,
                treeSelectData
            }
        }
    })
</script>
