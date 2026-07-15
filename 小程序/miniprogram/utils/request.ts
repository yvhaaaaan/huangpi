import { API_CONFIG, getApiBaseUrl } from '../config/api'

export type RequestMethod = 'GET' | 'POST' | 'PUT' | 'DELETE'

export interface PageResult<T> {
  list: T[]
  page: number
  pageSize: number
  total: number
}

export interface ApiEnvelope<T> {
  code: number
  message: string
  data: T
  traceId?: string
}

export interface RequestOptions {
  path: string
  method?: RequestMethod
  data?: WechatMiniprogram.RequestOption['data']
  auth?: boolean
  header?: Record<string, string>
}

export class ApiError extends Error {
  code: number
  statusCode?: number
  traceId?: string

  constructor(message: string, code = -1, statusCode?: number, traceId?: string) {
    super(message)
    this.name = 'ApiError'
    this.code = code
    this.statusCode = statusCode
    this.traceId = traceId
  }
}

const SESSION_KEY = 'auth.session.v2'
let redirectingToLogin = false

const getToken = (): string => {
  const session = wx.getStorageSync(SESSION_KEY) as { token?: string } | undefined
  return session && session.token ? session.token : ''
}

export const handleUnauthorized = (): void => {
  wx.removeStorageSync(SESSION_KEY)
  wx.removeStorageSync('auth.session.v1')
  wx.removeStorageSync('userProfile')
  getApp<IAppOption>().globalData.session = null
  if (redirectingToLogin) return
  redirectingToLogin = true
  wx.reLaunch({
    url: '/pages/login/index',
    complete: () => { redirectingToLogin = false },
  })
}

export const request = <T>(options: RequestOptions): Promise<T> => {
  const token = options.auth === false ? '' : getToken()
  const header: Record<string, string> = {
    'content-type': 'application/json',
    ...options.header,
  }
  if (token) header.Authorization = `Bearer ${token}`

  return new Promise<T>((resolve, reject) => {
    wx.request<ApiEnvelope<T>>({
      url: `${getApiBaseUrl()}${options.path}`,
      method: options.method || 'GET',
      data: options.data,
      header,
      timeout: API_CONFIG.timeout,
      success: response => {
        const body = response.data
        const bodyCode = body && body.code
        const bodyMessage = body && body.message
        const traceId = body && body.traceId
        if (response.statusCode === 401 || bodyCode === 40100) {
          handleUnauthorized()
          reject(new ApiError(bodyMessage || '登录状态已失效', bodyCode || 40100, response.statusCode, traceId))
          return
        }
        if (response.statusCode < 200 || response.statusCode >= 300) {
          reject(new ApiError(bodyMessage || `请求失败 (${response.statusCode})`, bodyCode || -1, response.statusCode, traceId))
          return
        }
        if (!body || body.code !== 0) {
          reject(new ApiError(bodyMessage || '接口返回异常', bodyCode || -1, response.statusCode, traceId))
          return
        }
        resolve(body.data)
      },
      fail: error => reject(new ApiError(error.errMsg || '网络连接失败')),
    })
  })
}
