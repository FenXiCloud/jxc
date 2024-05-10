/**
 * @功能描述: 退货单
 * @创建时间: 2024年05月09日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    save(param) {
        return Ajax.post('/purchaseRtOrder', param)
    },
    list(param) {
        return Ajax.get('/purchaseRtOrder', param)
    },
    remove(id) {
        return Ajax.delete('/purchaseRtOrder/' + id);
    },
    load(id) {
        return Ajax.get('/purchaseRtOrder/load/' + id);
    },
    select(param) {
        return Ajax.get('/purchaseRtOrder/select', param)
    },
    updateState(param) {
        return Ajax.put('/purchaseRtOrder', param)
    },
    levelPrice(rtOrderId) {
        return Ajax.get('/purchaseRtOrder/level/price/' + rtOrderId)
    },
}

