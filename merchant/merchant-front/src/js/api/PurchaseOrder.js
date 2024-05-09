/**
 * @功能描述: 入库单
 * @创建时间: 2024年05月09日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    save(param) {
        return Ajax.post('/purchaseOrder', param)
    },
    list(param) {
        return Ajax.get('/purchaseOrder', param)
    },
    remove(id) {
        return Ajax.delete('/purchaseOrder/' + id);
    },
    load(id) {
        return Ajax.get('/purchaseOrder/load/' + id);
    },
    loadTo(id) {
        return Ajax.get('/purchaseOrder/load/' + id);
    },
    select(param) {
        return Ajax.get('/purchaseOrder/select', param)
    },
    listBy(vendorsId) {
        return Ajax.get('/purchaseOrder/listBy/'+vendorsId)
    },
    updateState(param) {
        return Ajax.put('/purchaseOrder', param)
    },
    levelPrice(purchaseOrderId) {
        return Ajax.get('/purchaseOrder/level/price/' + purchaseOrderId)
    },
}

