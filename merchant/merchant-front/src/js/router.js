
import {defineAsyncComponent} from "vue";

const groupRoutes = [
	{name: 'DashboardMain', component: defineAsyncComponent(() => import("@components/group/dashboard/DashboardMain"))},
	{name: 'AccountBasic', component: defineAsyncComponent(() => import("@components/common/AccountBasic"))},

	{name: 'ProductsCategoryList', component: defineAsyncComponent(() => import('@components/group/basics/ProductsCategoryList'))},

	{name: 'CustomersList', component: defineAsyncComponent(() => import('@components/group/basics/CustomersList'))},

	{name: 'CustomersLevelList', component: defineAsyncComponent(() => import('@components/group/basics/CustomersLevelList'))},

	{name: 'CustomersCategoryList', component: defineAsyncComponent(() => import('@components/group/basics/CustomersCategoryList'))},

	{name: 'VendorsCategoryList', component: defineAsyncComponent(() => import('@components/group/basics/VendorsCategoryList'))},

	{name: 'VendorsList', component: defineAsyncComponent(() => import('@components/group/basics/VendorsList'))},

	{name: 'WarehousesList', component: defineAsyncComponent(() => import('@components/group/basics/WarehousesList'))},

	{name: 'UnitsList', component: defineAsyncComponent(() => import('@components/group/basics/UnitsList'))},

	{name: 'ProductsList', component: defineAsyncComponent(() => import('@components/group/basics/ProductsList'))},

	{name: 'OrganizationList', component: defineAsyncComponent(() => import('@components/group/setting/OrganizationList'))},

	{name: 'ArgsSetting', component: defineAsyncComponent(() => import('@components/group/setting/ArgsSetting'))},

	{name: 'MerchantInfo', component: defineAsyncComponent(() => import('@components/group/setting/MerchantInfo'))},

	{name: 'AdminList', component: defineAsyncComponent(() => import('@components/group/setting/AdminList'))},

	{name: 'RoleList', component: defineAsyncComponent(() => import('@components/group/setting/RoleList'))},

	{name: 'CheckoutList', component: defineAsyncComponent(() => import('@components/group/setting/CheckoutList'))},

	{name: 'SalesOrderList', component: defineAsyncComponent(() => import('@components/group/sales/SalesOrderList'))},

	{name: 'SalesDetail', component: defineAsyncComponent(() => import('@components/group/sales/SalesDetail'))},

	{name: 'SalesSummary', component: defineAsyncComponent(() => import('@components/group/sales/SalesSummary'))},

	{name: 'SalesReturnList', component: defineAsyncComponent(() => import('@components/group/sales/SalesReturnList'))},

	{name: 'PurchaseDetail', component: defineAsyncComponent(() => import('@components/group/purchase/PurchaseDetail'))},

	{name: 'PurchaseSummary', component: defineAsyncComponent(() => import('@components/group/purchase/PurchaseSummary'))},

	{name: 'PurchaseOrderList', component: defineAsyncComponent(() => import('@components/group/purchase/PurchaseOrderList'))},

	{name: 'PurchaseReturnList', component: defineAsyncComponent(() => import('@components/group/purchase/PurchaseReturnList'))},

	{name: 'Stock', component: defineAsyncComponent(() => import('@components/group/stock/Stock'))},

	{name: 'StockSummary', component: defineAsyncComponent(() => import('@components/group/stock/StockSummary'))},

	{name: 'StockDetail', component: defineAsyncComponent(() => import('@components/group/stock/StockDetail'))},

	{name: 'StockCostAdjustmentList', component: defineAsyncComponent(() => import('@components/group/stock/StockCostAdjustmentList'))},

	{name: 'StockInboundList', component: defineAsyncComponent(() => import('@components/group/stock/StockInboundList'))},

	{name: 'StockCostAdjustmentView', component: defineAsyncComponent(() => import('@components/group/stock/StockCostAdjustmentView'))},

	{name: 'StockInventoryList', component: defineAsyncComponent(() => import('@components/group/stock/StockInventoryList'))},

	{name: 'StockTransferList', component: defineAsyncComponent(() => import('@components/group/stock/StockTransferList'))},

	{name: 'StockOutboundList', component: defineAsyncComponent(() => import('@components/group/stock/StockOutboundList'))},

	{name: 'SalesRankingsList', component: defineAsyncComponent(() => import('@components/group/sales/SalesRankingsList'))},

	{name: 'SalesProfitList', component: defineAsyncComponent(() => import('@components/group/sales/SalesProfitList'))},

]

/**
 * 加载组件
 */
export default {
	install: (app) => {
		groupRoutes.forEach(item => app.component(item.name, item.component))
	}

}

