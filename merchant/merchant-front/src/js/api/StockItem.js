/**
 * @功能描述: 库存明细
 * @创建时间: 2024年05月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    detail(param) {
        return Ajax.get('/stockItem/detail', param)
    },
    adjustmentDetail(param) {
        return Ajax.get('/stockItem/adjustment/detail', param)
    },
    byProducts(param) {
        return Ajax.get('/stockItem/byProducts', param)
    },
}

