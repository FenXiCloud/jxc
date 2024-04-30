import {createRouter, createWebHistory} from 'vue-router'
import {loadingBar} from "heyui";

import Login from '@components/Login';
import DashboardMain from '@components/group/dashboard/DashboardMain';
import MerchantInfo from '@components/group/setting/MerchantInfo';
import AdminList from '@components/group/setting/AdminList';
import RoleList from '@components/group/setting/RoleList';
import AccountBasic from '@components/common/AccountBasic';
import CustomersList from '@components/group/basics/CustomersList';
import OrganizationList from '@components/group/setting/OrganizationList';
import CustomersLevelList from '@components/group/basics/CustomersLevelList';
import CustomersCategoryList from '@components/group/basics/CustomersCategoryList';
import VendorsCategoryList from '@components/group/basics/VendorsCategoryList';
import VendorsList from '@components/group/basics/VendorsList';
import WarehousesList from '@components/group/basics/WarehousesList';
import UnitsList from '@components/group/basics/UnitsList';
import ProductsList from '@components/group/basics/ProductsList';
import ProductsCategoryList from '@components/group/basics/ProductsCategoryList';

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
				path: 'organizationList',
				name: 'OrganizationList',
				component: OrganizationList,
				meta: {title: '组织机构', icon: 'icon-monitor'}
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
				path: 'customersCategoryList',
				name: 'CustomersCategoryList',
				component: CustomersCategoryList,
				meta: {title: '客户分类', icon: 'icon-monitor'}
			},
			{
				path: 'customersLevelList',
				name: 'CustomersLevelList',
				component: CustomersLevelList,
				meta: {title: '客户等级', icon: 'icon-monitor'}
			},
			{
				path: 'customersList',
				name: 'CustomersList',
				component: CustomersList,
				meta: {title: '客户档案', icon: 'icon-monitor'}
			},
			{
				path: 'vendorsCategoryList',
				name: 'VendorsCategoryList',
				component: VendorsCategoryList,
				meta: {title: '货商分类', icon: 'icon-monitor'}
			},
			{
				path: 'vendorsList',
				name: 'VendorsList',
				component: VendorsList,
				meta: {title: '货商档案', icon: 'icon-monitor'}
			},{
				path: 'warehousesList',
				name: 'WarehousesList',
				component: WarehousesList,
				meta: {title: '仓库档案', icon: 'icon-monitor'}
			},{
				path: 'unitsList',
				name: 'UnitsList',
				component: UnitsList,
				meta: {title: '计量单位', icon: 'icon-monitor'}
			},{
				path: 'productsList',
				name: 'ProductsList',
				component: ProductsList,
				meta: {title: '商品档案', icon: 'icon-monitor'}
			},{
				path: 'productsCategoryList',
				name: 'ProductsCategoryList',
				component: ProductsCategoryList,
				meta: {title: '商品分类', icon: 'icon-monitor'}
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
            document.title = to.meta.title + ' - 纷析云';
        } else {
            document.title = '纷析云';
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
