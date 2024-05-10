/**
 * @功能描述: 销售退货单
 * @创建时间: 2024年05月09日
 * @公司官网: www.fenxi365.com
 * @公司信息: 纷析云（杭州）科技有限公司
 * @公司介绍: 专注于财务相关软件开发, 企业会计自动化解决方案
 */
import Ajax from "@common/Request";

export default {
    save(param) {
        return Ajax.post('/salesReturn', param)
    },
    list(param) {
        return Ajax.get('/salesReturn', param)
    },
    remove(id) {
        return Ajax.delete('/salesReturn/' + id);
    },
    load(id) {
        return Ajax.get('/salesReturn/load/' + id);
    },
    select(param) {
        return Ajax.get('/salesReturn/select', param)
    },
    updateState(param) {
        return Ajax.put('/salesReturn', param)
    },
}

