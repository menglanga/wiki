<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff',padding: '24px', margin: 0, minHeight: '280px'}">
            <h3 v-if="level1.length===0">对不起，找不到相关文档</h3>
           <a-row>
               <a-col :span="6">
                   <a-tree
                    v-if="level1.length>0"
                    :tree-data="level1"
                    @select="onSelect"
                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                    :defaultExpandAll="true"
                    :defaultSelectedKeys="defaultSelectedKeys"
                   >

                   </a-tree>
               </a-col>
               <a-col :span="18">
                   <div>
                       <h2>{{doc.name}}</h2>
                       <div>
                           <span>阅读数：{{doc.viewCount}}</span>&nbsp;&nbsp;
                           <span>点赞数：{{doc.voteCount}}</span>
                       </div>
                       <a-divider style="height: 2px; background-color: black"/>
                   </div>
                   <div :innerHTML="html"></div>
                   <div class="vote-div">
                       <a-button type="primary" shape="round" :size="'large'" @click="vote">
                           <template #icon><LikeOutLined/>&nbsp;点赞:{{doc.voteCount}}</template>
                       </a-button>
                   </div>
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

            const html=ref();

            const defaultSelectedKeys=ref();
            defaultSelectedKeys.value=[];

            const doc=ref();
            doc.value={};

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

            //  富文本内容查询
            const handleQueryContent = (id :number) => {

                axios.get("/doc/find-content/"+id).then((response) => {

                    const data = response.data;
                    if (data.success) {
                        html.value=data.data;
                    } else {
                        message.error(data.message);
                    }


                });
            };

            //  数据查询
            const handleQuery = () => {

                axios.get("/doc/all/"+route.query.ebookId).then((response) => {

                    const data = response.data;
                    if (data.success) {
                        docs.value = data.data;

                        level1.value = [];
                        level1.value = Tool.array2tree(docs.value, 0);

                        if (Tool.isNotEmpty(level1)){
                            defaultSelectedKeys.value=[level1.value[0].id];
                            handleQueryContent(level1.value[0].id);
                            //初始显示文档信息
                            doc.value=level1.value[0];
                        }
                    } else {
                        message.error(data.message);
                    }


                });
            };






            const  onSelect=(selectedKeys: any , info : any)=>{
                console.log('selected',selectedKeys,info);
                if (Tool.isNotEmpty(selectedKeys)) {
                    //选中某一节点时加载文档信息
                    doc.value=info.selectedNodes[0].props;
                    //加载内容
                    handleQueryContent(selectedKeys[0]);
                }
            };

            const vote=()=>{
                axios.get('/doc/vote/'+doc.value.id).then((response)=>{
                    const data=response.data;
                    if (data.success){
                        doc.value.voteCount++;
                    } else{
                        message.error(data.message);
                    }
                })
            };


            onMounted(() => {
                handleQuery();
            });

            return{
                level1,
                html,
                onSelect,
                defaultSelectedKeys,
                doc,
                vote
            }
        }
    });



</script>

<style>
    .vote-div{
        padding: 15px;
        text-align:center;
    }
</style>