import {createApp} from 'vue'
import App from './components/App.vue'
import router from './js/router'
import store from './js/store'
import {useTable} from '@common/xe-table'
import heyui from 'heyui';

import './style/app.less'
import 'font-awesome/css/font-awesome.css'
import 'windi.css'
import layer from '@layui/layer-vue';
import '@layui/layer-vue/lib/index.css';
import directive from '@/js/directive'
import '@common/dict';

let app = createApp(App);
app.use(layer).use(store).use(useTable).use(heyui).directive("auth",directive);
store.dispatch('init').then((account) => {
	app.use(router(account.group));
	app.mount('#app')
}).catch(() => {
	let routerInst = router(false);
	app.use(routerInst);
	app.mount('#app')
	routerInst.push("/login")
});

