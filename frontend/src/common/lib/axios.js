
import VueAxios from 'vue-axios'
import axios from 'axios'
// import config from '../config'
import store from '@/views/main/store/index'

const BASE_URL = '/api/v1'
const DEFAULT_ACCEPT_TYPE = 'application/json'

axios.defaults.baseURL = BASE_URL
axios.defaults.headers['Content-Type'] = DEFAULT_ACCEPT_TYPE

const axiosInstance = axios.create({
})

axiosInstance.interceptors.request.use(
    config => {
        store.commit('startSpinner');
        return config;
    },
    error => {
        alert('데이터 요청 실패');
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    response => {
        store.commit('endSpinner');
        return response;
    },
    error => {
        alert('데이터 응답 실패');
        return Promise.reject(error);
    }
)

export default { VueAxios, axios , axiosInstance }
