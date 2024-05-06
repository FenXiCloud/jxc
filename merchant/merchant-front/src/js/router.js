import {createRouter, createWebHistory} from 'vue-router'
import {loadingBar} from "heyui.ext";

import Login from '@components/Login';
import DashboardMain from '@components/group/dashboard/DashboardMain';
import MerchantInfo from '@components/group/setting/MerchantInfo';
import AdminList from '@components/group/setting/AdminList';
import RoleList from '@components/group/setting/RoleList';
import CheckoutList from '@components/group/setting/CheckoutList';
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

import SalesOrderForm from '@components/group/sales/SalesOrderForm';
import SalesOrderList from '@components/group/sales/SalesOrderList';
import SalesDetail from '@components/group/sales/SalesDetail';
import SalesSummary from '@components/group/sales/SalesSummary';
import SalesReturnForm from '@components/group/sales/SalesReturnForm';
import SalesReturnList from '@components/group/sales/SalesReturnList';

import PurchaseDetail from '@components/group/purchase/PurchaseDetail';
import PurchaseSummary from '@components/group/purchase/PurchaseSummary';
import PurchaseOrderForm from '@components/group/purchase/PurchaseOrderForm';
import PurchaseOrderList from '@components/group/purchase/PurchaseOrderList';
import PurchaseReturnForm from '@components/group/purchase/PurchaseReturnForm';
import PurchaseReturnList from '@components/group/purchase/PurchaseReturnList';

import Stock from '@components/group/stock/Stock';
import StockDetail from '@components/group/stock/StockDetail';
import StockSummary from '@components/group/stock/StockSummary';
import StockCostAdjustmentForm from '@components/group/stock/StockCostAdjustmentForm';
import StockCostAdjustmentList from '@components/group/stock/StockCostAdjustmentList';
import StockCountForm from '@components/group/stock/StockCountForm';
import StockCountList from '@components/group/stock/StockCountList';
import StockInboundForm from '@components/group/stock/StockInboundForm';
import StockInboundList from '@components/group/stock/StockInboundList';
import StockOutboundForm from '@components/group/stock/StockOutboundForm';
import StockOutboundList from '@components/group/stock/StockOutboundList';
import StockTransferForm from '@components/group/stock/StockTransferForm';
import StockTransferList from '@components/group/stock/StockTransferList';


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
				path: 'checkoutList',
				name: 'CheckoutList',
				component: CheckoutList,
				meta: {title: '结账列表', icon: 'icon-monitor'}
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

			{
				path: 'salesDeliveryForm',
				name: 'SalesDeliveryForm',
				component: SalesOrderForm,
				meta: {title: '销售出库单', icon: 'icon-monitor'}
			},
			{
				path: 'salesDeliveryList',
				name: 'SalesDeliveryList',
				component: SalesOrderList,
				meta: {title: '销售出库单列表', icon: 'icon-monitor'}
			},
			{
				path: 'salesReturnForm',
				name: 'SalesReturnForm',
				component: SalesReturnForm,
				meta: {title: '销售退货单', icon: 'icon-monitor'}
			},
			{
				path: 'salesReturnList',
				name: 'SalesReturnList',
				component: SalesReturnList,
				meta: {title: '销售退货单列表', icon: 'icon-monitor'}
			},
			{
				path: 'salesDetail',
				name: 'SalesDetail',
				component: SalesDetail,
				meta: {title: '销售明细表', icon: 'icon-monitor'}
			},
			{
				path: 'salesSummary',
				name: 'SalesSummary',
				component: SalesSummary,
				meta: {title: '销售汇总表', icon: 'icon-monitor'}
			},

			{
				path: 'purchaseDetail',
				name: 'PurchaseDetail',
				component: PurchaseDetail,
				meta: {title: '采购明细表', icon: 'icon-monitor'}
			},
			{
				path: 'purchaseSummary',
				name: 'PurchaseSummary',
				component: PurchaseSummary,
				meta: {title: '采购汇总表', icon: 'icon-monitor'}
			},
			{
				path: 'purchaseReceiptForm',
				name: 'PurchaseReceiptForm',
				component: PurchaseOrderForm,
				meta: {title: '采购入库单', icon: 'icon-monitor'}
			},
			{
				path: 'purchaseReceiptList',
				name: 'PurchaseReceiptList',
				component: PurchaseOrderList,
				meta: {title: '采购入库单列表', icon: 'icon-monitor'}
			},
			{
				path: 'purchaseReturnForm',
				name: 'PurchaseReturnForm',
				component: PurchaseReturnForm,
				meta: {title: '采购退货单', icon: 'icon-monitor'}
			},
			{
				path: 'purchaseReturnList',
				name: 'PurchaseReturnList',
				component: PurchaseReturnList,
				meta: {title: '采购退货单列表', icon: 'icon-monitor'}
			},

			{
				path: 'Stock',
				name: 'Stock',
				component: Stock,
				meta: {title: '库存查询', icon: 'icon-monitor'}
			},
			{
				path: 'StockDetail',
				name: 'StockDetail',
				component: StockDetail,
				meta: {title: '进销存明细表', icon: 'icon-monitor'}
			},
			{
				path: 'StockSummary',
				name: 'StockSummary',
				component: StockSummary,
				meta: {title: '进销存汇总表', icon: 'icon-monitor'}
			},
			{
				path: 'StockCostAdjustmentForm',
				name: 'StockCostAdjustmentForm',
				component: StockCostAdjustmentForm,
				meta: {title: '成本调整单', icon: 'icon-monitor'}
			},
			{
				path: 'StockCostAdjustmentList',
				name: 'StockCostAdjustmentList',
				component: StockCostAdjustmentList,
				meta: {title: '成本调整单列表', icon: 'icon-monitor'}
			},
			{
				path: 'StockCountForm',
				name: 'StockCountForm',
				component: StockCountForm,
				meta: {title: '库存盘点单', icon: 'icon-monitor'}
			},
			{
				path: 'StockCountList',
				name: 'StockCountList',
				component: StockCountList,
				meta: {title: '库存盘点单列表', icon: 'icon-monitor'}
			},
			{
				path: 'StockInboundForm',
				name: 'StockInboundForm',
				component: StockInboundForm,
				meta: {title: '其他入库单', icon: 'icon-monitor'}
			},
			{
				path: 'StockInboundList',
				name: 'StockInboundList',
				component: StockInboundList,
				meta: {title: '其他入库单列表', icon: 'icon-monitor'}
			},
			{
				path: 'StockOutboundForm',
				name: 'StockOutboundForm',
				component: StockOutboundForm,
				meta: {title: '其他出库单', icon: 'icon-monitor'}
			},
			{
				path: 'StockOutboundList',
				name: 'StockOutboundList',
				component: StockOutboundList,
				meta: {title: '其他出库单列表', icon: 'icon-monitor'}
			},
			{
				path: 'StockTransferForm',
				name: 'StockTransferForm',
				component: StockTransferForm,
				meta: {title: '库存调拨单', icon: 'icon-monitor'}
			},
			{
				path: 'StockTransferList',
				name: 'StockTransferList',
				component: StockTransferList,
				meta: {title: '库存调拨单列表', icon: 'icon-monitor'}
			}
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
