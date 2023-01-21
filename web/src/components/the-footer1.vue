<template>
    <a-layout-footer style="text-align: center">
        JAVA<span v-if="user.id">,你好{{user.name}}</span>
    </a-layout-footer>
</template>

<script lang="ts">
    import {defineComponent, computed,onMounted} from 'vue';
    import store from "@/store";
    import {Tool} from "@/util/tool";
    import { notification } from 'ant-design-vue';

    export default defineComponent({
        name: 'the-footer1',
        setup() {
            const user = computed(() => store.state.user);

            let websocket: any;
            let token:any;
            const onOpen=()=>{
                console.log('websocket连接成功，状态码：',websocket.readyState);
            };

            const onMessage=(event:any)=>{
                console.log('websocket收到消息',event.data);
                notification['info']({
                    message: '收到消息',
                    description: event.data
                });

            };

            const onError=()=>{
                console.log('websocket连接错误，状态码：',websocket.readyState);
            };

            const onClose=()=>{
                console.log('websocket连接关闭，状态码：',websocket.readyState);
            };

            const initWebSocket=()=>{
                websocket.onopen=onOpen;
                websocket.onclose=onClose;
                websocket.onmessage=onMessage;
                websocket.onerror=onError;


            };

            onMounted(() => {
                if ('WebSocket' in window){
                    token=Tool.uuid(10);
                    //地址：ws://127.0.0.1:8888/ws/xxx
                    websocket=new WebSocket(process.env.VUE_APP_WS_SERVER+'/ws/'+token);
                    initWebSocket();
                    // websocket.close();
                }else {
                    alert("当前浏览器不支持");
                }
            });

            return {
                user
            };

        }
    });
</script>