<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff',padding: '24px', margin: 0, minHeight: '280px'}">
           <a-row>
               <a-col :span="6">
                   <a-tree
                    v-if="level1.length>0"
                    :tree-data="level1"
                    @select="onSelect"
                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                    :defaultExpandAll="true"
                   >

                   </a-tree>
               </a-col>
               <a-col :span="18">

               </a-col>
           </a-row>
        </a-layout-content>
    </a-layout>
</template>


<script lang="ts">
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from 'axios';
    import {message} from 'ant-design-vue';
    import {Tool} from "@/util/tool";
    import {useRoute} from "vue-router";
    import E from "wangeditor";

    export default defineComponent({
        name: 'Doc',
        setup() {
            const route = useRoute();

            const docs = ref();

            // const columns = [
            //     {
            //         title: '名称',
            //         dataIndex: 'name',
            //         slots: {customRender: 'name'}
            //     },
            //     // {
            //     //     title: '父文档',
            //     //     key: 'parent',
            //     //     dataIndex: 'parent'
            //     // },
            //     // {
            //     //     title: '顺序',
            //     //     dataIndex: 'sort',
            //     // },
            //     {
            //         title: 'Action',
            //         key: 'action',
            //         slots: {customRender: 'action'}
            //     }
            // ];

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
            level1.value = [];

            //  数据查询
            const handleQuery = () => {

                axios.get("/doc/all").then((response) => {

                    const data = response.data;
                    if (data.success) {
                        docs.value = data.data;

                        level1.value = [];
                        level1.value = Tool.array2tree(docs.value, 0);
                    } else {
                        message.error(data.message);
                    }


                });
            };

            onMounted(() => {
                handleQuery();
            });

            return{
                level1
            }
        }
    });



</script>