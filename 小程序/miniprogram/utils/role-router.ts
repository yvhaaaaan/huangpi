import { AuthSession, clearAuthSession, getAuthSession, UserRole } from './auth'

export const getRoleHome = (role: UserRole): string => {
  if (role === 'merchant') return '/pages/merchant/index'
  if (role === 'admin') return '/pages/admin/index'
  return '/pages/home/index'
}

export const enterRoleHome = (session: AuthSession): void => {
  wx.reLaunch({ url: getRoleHome(session.user.role) })
}

export const requireAuth = (allowedRoles?: UserRole[]): AuthSession | null => {
  const session = getAuthSession()
  if (!session) {
    wx.reLaunch({ url: '/pages/login/index' })
    return null
  }
  if (allowedRoles && !allowedRoles.includes(session.user.role)) {
    wx.reLaunch({ url: getRoleHome(session.user.role) })
    return null
  }
  return session
}

export const logout = (): void => {
  clearAuthSession()
  wx.reLaunch({ url: '/pages/login/index' })
}
