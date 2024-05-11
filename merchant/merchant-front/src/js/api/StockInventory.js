/**
 * @功能描述: 盘点单
 * @创建时间: 2024年05月11日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    save(param) {
        return Ajax.post('/stockInventory', param)
    },
    list(param) {
        return Ajax.get('/stockInventory', param)
    },
    productsSelect(param) {
        return Ajax.get('/stockInventory/productsSelect', param)
    },
    remove(id) {
        return Ajax.delete('/stockInventory/' + id);
    },
    load(id) {
        return Ajax.get('/stockInventory/load/' + id);
    },
}

