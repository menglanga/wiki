<template>
    <a-layout-header class="header">
        <div class="logo"></div>
        <a-menu
                theme="dark"
                mode="horizontal"
                v-model:selectedKeys="selectedKeys1"
                :style="{ lineHeight: '64px' }"
        >
            <a-menu-item key="/">
                <router-link to="/">首页</router-link>
            </a-menu-item>

            <a-menu-item key="/admin/user" v-if="user.id">
                <router-link to="/admin/user">用户管理</router-link>
            </a-menu-item>

            <a-menu-item key="/admin/ebook" v-if="user.id">
                <router-link to="/admin/ebook">电子书管理</router-link>
            </a-menu-item>

            <a-menu-item key="/admin/category" v-if="user.id">
                <router-link to="/admin/category">分类管理</router-link>
            </a-menu-item>

            <a-menu-item key="/about">
                <router-link to="/about">关于我们</router-link>
            </a-menu-item>

            <a-popconfirm
                    title="是否退出登录？"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="logout()"
            >
                <a class="login-menu" v-if="user.id">
                    <span>退出登录</span>
                </a>
            </a-popconfirm>

            <a class="login-menu" v-if="user.id">
                <span>欢迎您，{{user.name}}</span>
            </a>
            <a class="login-menu" v-if="!user.id" @click="showLoginModel">
                <span>登录</span>
            </a>
        </a-menu>

        <a-modal
            title="登录"
            v-model:visible="loginModelVisible"
            :confirm-loading="loginModelLoading"
            @ok="login"
        >
            <a-form :model="loginUser" :label-col="{span: 6}">
                <a-form-item label="登录名">
                    <a-input v-model:value="loginUser.loginName" />
                </a-form-item>
                <a-form-item label="密码">
                    <a-input v-model:value="loginUser.password" type="password" />
                </a-form-item>
            </a-form>
        </a-modal>
    </a-layout-header>
</template>

<script lang="ts">
    import { defineComponent,ref,computed } from 'vue';
    import axios from 'axios';
    import {message} from 'ant-design-vue';
    import store from "@/store";

    declare let hexMd5: any;
    declare let KEY: any;

    export default defineComponent({
        name: 'the-header',
        setup(){
            const loginUser=ref({
                loginName: "",
                password: "",
            });

            //登录后用户信息
            const user = computed(() => store.state.user);

            const loginModelVisible=ref(false);
            const loginModelLoading=ref(false);
            const showLoginModel=()=>{
                loginModelVisible.value=true;
            };

            const login=()=>{
                console.log("开始登录");

                loginModelLoading.value = true;

                loginUser.value.password=hexMd5(loginUser.value.password+KEY);

                axios.post('/user/login', loginUser.value).then((response) => {
                    loginModelLoading.value = false;
                    const data = response.data;
                    if (data.success) {
                        loginModelVisible.value = false;

                        message.success("登录成功！");
                        // user.value=data.data;
                        store.commit("setUser",data.data);
                    } else {
                        message.error(data.message);
                    }
                });
            };

            const logout=()=>{
                console.log("退出登录");
                axios.get('/user/logout/'+ user.value.token).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        message.success("退出登录成功！");
                        // user.value=data.data;
                        store.commit("setUser",{});
                    } else {
                        message.error(data.message);
                    }
                });
            };

            return{
                loginModelVisible,
                loginModelLoading,
                showLoginModel,
                loginUser,
                login,
                user,
                logout
            }
        }
    });
</script>

<style scoped>
    .login-menu{
        float: right ;
        color: white;
        width: 120px;
    }

</style>