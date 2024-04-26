import {createRouter, createWebHistory} from 'vue-router'
import {loadingBar} from "heyui";

import Login from '@components/Login';
import DashboardMain from '@components/group/dashboard/DashboardMain';
import MerchantInfo from '@components/group/setting/MerchantInfo';
import AdminList from '@components/group/setting/AdminList';
import RoleList from '@components/group/setting/RoleList';
import AccountBasic from '@components/common/AccountBasic';

import CustomersList from '@components/group/basics/CustomersList';
import CommodityList from '@components/group/basics/CommodityList';
import InvoiceConfig from '@components/group/basics/InvoiceConfig';
import UserAccountList from '@components/group/basics/UserAccountList';

import FastInvoice from '@components/group/invoice/FastInvoice';
import InvoiceList from '@components/group/order/InvoiceList';
import OrderList from '@components/group/order/OrderList';

const groupRoutes = [
	{
		path: '/login',
		name: 'Login',
		component: Login
	},
	{
		path: '/',
		name: 'AppFrame',
		component: () => import('@components/app/AppFrame'),
		children: [
			{
				path: '',
				name: 'DashboardMain',
				component: DashboardMain,
				meta: {title: '桌面', icon: 'icon-monitor'}
			},
			{
				path: 'merchant/info',
				name: 'MerchantInfo',
				component: MerchantInfo,
				meta: {title: '商户信息', icon: 'icon-monitor'}
			},
			{
				path: 'admin',
				name: 'AdminList',
				component: AdminList,
				meta: {title: '用户管理', icon: 'icon-monitor'}
			},
			{
				path: 'role',
				name: 'RoleList',
				component: RoleList,
				meta: {title: '角色', icon: 'icon-monitor'}
			},
			{
				path: 'account',
				name: 'AccountBasic',
				component: AccountBasic,
				meta: {title: '账号信息', icon: 'icon-monitor'}
			},
			{
				path: 'customersList',
				name: 'CustomersList',
				component: CustomersList,
				meta: {title: '常用客户', icon: 'icon-monitor'}
			},
			{
				path: 'commodityList',
				name: 'CommodityList',
				component: CommodityList,
				meta: {title: '常用商品', icon: 'icon-monitor'}
			},
			{
				path: 'invoiceConfig',
				name: 'InvoiceConfig',
				component: InvoiceConfig,
				meta: {title: '开票设置', icon: 'icon-monitor'}
			},
			{
				path: 'userAccountList',
				name: 'UserAccountList',
				component: UserAccountList,
				meta: {title: '数电账户管理', icon: 'icon-monitor'}
			},{
				path: 'fastInvoice',
				name: 'FastInvoice',
				component: FastInvoice,
				meta: {title: '便捷开票', icon: 'icon-monitor'}
			},
			{
				path: 'invoiceList',
				name: 'InvoiceList',
				component: InvoiceList,
				meta: {title: '发票查询', icon: 'icon-monitor'}
			},
			{
				path: 'orderList',
				name: 'OrderList',
				component: OrderList,
				meta: {title: '订单查询', icon: 'icon-monitor'}
			},
		]
	}, {
		path: '/permission/error',
		name: 'PermissionError',
		component: () => import('@components/common/PermissionError')
	}
]


/**
 * 创建路由
 * @param isGroup
 * @returns {Router}
 */
const create = () => {
    let router = createRouter({
        history: createWebHistory(process.env.BASE_URL),
        routes: groupRoutes
    })

    let isFirstRouter = true;

    router.beforeEach((to, from, next) => {
        loadingBar.start();
        if (to.meta && to.meta.title) {
            document.title = to.meta.title + ' - 企万慧';
        } else {
            document.title = '企万慧';
        }
        isFirstRouter = false;
        next();
    });

    router.afterEach(() => {
        loadingBar.success();
        document.documentElement.scrollTop = 0;
        document.body.scrollTop = 0;
        let layoutContent = document.querySelector('.h-layout-content');
        if (layoutContent) {
            layoutContent.scrollTop = 0;
        }
    });
    return router;
}

export default create
