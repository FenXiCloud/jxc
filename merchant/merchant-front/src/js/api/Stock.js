/**
 * @功能描述: 库存管理
 * @创建时间: 2024年05月08日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    list(param) {
        return Ajax.get('/stock', param)
    },
    adjustment(param) {
        return Ajax.get('/stock/adjustment', param)
    },
}

