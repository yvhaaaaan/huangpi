import { request } from '../utils/request'

export type ApiUserRole = 'user' | 'merchant' | 'admin'

export interface ApiAuthSession {
  token: string
  expiresAt: number
  user: {
    id: string
    nickname: string
    role: ApiUserRole
    merchantId?: string
  }
}

export const loginWithWechat = (code: string): Promise<ApiAuthSession> => request<ApiAuthSession>({
  path: '/api/auth/wechat-login',
  method: 'POST',
  data: { code },
  auth: false,
})

export const loginWithAccount = (account: string, password: string): Promise<ApiAuthSession> => request<ApiAuthSession>({
  path: '/api/auth/account-login',
  method: 'POST',
  data: { account, password },
  auth: false,
})

export const getRemoteSession = (): Promise<ApiAuthSession> => request<ApiAuthSession>({ path: '/api/auth/session' })
export const logoutRemoteSession = (): Promise<void> => request<void>({ path: '/api/auth/logout', method: 'POST' })

