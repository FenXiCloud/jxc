import {createStore} from 'vuex'
import {toArrayTree} from 'xe-utils'
import {Init} from "@js/api/App";
import manba from "manba";

export default createStore({
  state: {
    siderCollapsed: false,
    user: {},
    menus: [],
    orgs: [],
    org: {},
    granted: [],
    currentTab:  'DashboardMain',
  },
  mutations: {
    updateMenus(state, data) {
      state.menus = data;
    },
    updateOrgs(state, {orgs}) {
      state.orgs = orgs;
      let org = orgs.find(val=>val.current===true)
      org.checkoutSDate = manba(org.checkoutDate).add(1,manba.DAY).format("YYYY-MM-dd")
      state.org = org
    },
    updateOrg(state, checkoutDate) {
      if(checkoutDate){
        state.org.checkoutDate = checkoutDate
        state.org.checkoutSDate = manba(checkoutDate).add(1,manba.DAY).format("YYYY-MM-dd")
      }
    },
    updateAccount(state, { account}) {
      state.user = account;
      state.granted = account.granted || [];
    },
    updateSiderCollapse(state, isShow) {
      setTimeout(() => {
        state.pageResizeCount += 1;
      }, 600);
      state.siderCollapsed = isShow;
    },
    newTab(state, key) {
      state.currentTab = key;
    },
  },
  actions: {
    init({commit}) {
      return new Promise((resolve, reject) => {
        Init().then(({success, data}) => {
          if (success) {
            commit('updateAccount', data);
            commit('updateOrgs', data);
            commit('updateMenus', toArrayTree(data.menus, {strict: true}));
            resolve(data.account);
          } else {
            reject();
          }
        }).catch(() => {
          reject();
        })
      })
    },
    updateSiderCollapse({commit}, data) {
      commit('updateSiderCollapse', data);
    }
  },
  getters: {
    account: state => {
      return state.user;
    },
    siderCollapsed: state => {
      return state.siderCollapsed;
    }
  }
})
