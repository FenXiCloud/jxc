/**
 * @功能描述: 调拨单
 * @创建时间: 2024年05月10日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    save(param) {
        return Ajax.post('/StockTransfer', param)
    },
    list(param) {
        return Ajax.get('/StockTransfer', param)
    },
    remove(id) {
        return Ajax.delete('/StockTransfer/' + id);
    },
    load(id) {
        return Ajax.get('/StockTransfer/load/' + id);
    },
    select(param) {
        return Ajax.get('/StockTransfer/select', param)
    },
    listBy(vendorsId) {
        return Ajax.get('/StockTransfer/listBy/'+vendorsId)
    },
    updateState(param) {
        return Ajax.put('/StockTransfer', param)
    },
}

