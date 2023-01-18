<template>
    <a-layout-footer style="text-align: center">
        JAVA<span v-if="user.id">,你好{{user.name}}</span>
    </a-layout-footer>
</template>

<script lang="ts">
    import {defineComponent, computed} from 'vue';
    import store from "@/store";

    export default defineComponent({
        name: 'the-footer1',
        setup() {
            const user = computed(() => store.state.user);
            return {
                user
            }
        }
    });
</script>