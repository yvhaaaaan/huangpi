export type UserRole = 'user' | 'merchant' | 'admin'

export interface AuthUser {
  id: string
  nickname: string
  role: UserRole
  merchantId?: string
}

export interface AuthSession {
  token: string
  expiresAt: number
  user: AuthUser
}

const SESSION_KEY = 'auth.session.v2'
const SESSION_DURATION = 7 * 24 * 60 * 60 * 1000

export const getAuthSession = (): AuthSession | null => {
  const session = wx.getStorageSync(SESSION_KEY) as AuthSession | undefined
  if (!session?.token || !session.user?.role || session.expiresAt <= Date.now()) {
    if (session) wx.removeStorageSync(SESSION_KEY)
    return null
  }
  return session
}

export const saveAuthSession = (session: AuthSession): void => {
  wx.setStorageSync(SESSION_KEY, session)
}

export const clearAuthSession = (): void => {
  wx.removeStorageSync(SESSION_KEY)
  wx.removeStorageSync('auth.session.v1')
  wx.removeStorageSync('userProfile')
  wx.removeStorageSync('editProductId')
  wx.removeStorageSync('merchantLastSubmit')
}

const createMockSession = (account: string, nickname: string): AuthSession => {
  const isMerchant = account === 'merchant'
  const isAdmin = account === 'admin'
  const role: UserRole = isAdmin ? 'admin' : isMerchant ? 'merchant' : 'user'
  return {
    token: `demo-token-${Date.now()}`,
    expiresAt: Date.now() + SESSION_DURATION,
    user: {
      id: isAdmin ? 'admin-user-001' : isMerchant ? 'merchant-user-001' : 'wechat-user-001',
      nickname,
      role,
      ...(isMerchant ? { merchantId: 'merchant-demo-001' } : {}),
    },
  }
}

export const loginByAccount = async (account: string, password: string): Promise<AuthSession> => {
  if (!['merchant', 'admin'].includes(account) || password !== '123456') {
    throw new Error('INVALID_CREDENTIALS')
  }
  const nickname = account === 'admin' ? '黄陂镇文旅管理员' : '黄陂特色产品商家'
  const session = createMockSession(account, nickname)
  saveAuthSession(session)
  return session
}

export const loginByWechat = async (): Promise<AuthSession> => {
  await new Promise<void>((resolve, reject) => {
    wx.login({ success: () => resolve(), fail: error => reject(error) })
  })
  const session = createMockSession('wechat', '微信用户')
  saveAuthSession(session)
  return session
}
