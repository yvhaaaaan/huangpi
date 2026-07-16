export const API_CONFIG = {
  // Replace with the HTTPS domain configured in WeChat Mini Program settings.
  baseUrl: 'https://api.example.com',
  useMock: true,
  timeout: 10000,
}

const API_BASE_URL_STORAGE_KEY = 'api.baseUrl.override'

export const getApiBaseUrl = (): string => {
  const override = wx.getStorageSync(API_BASE_URL_STORAGE_KEY) as string | undefined
  return (override || API_CONFIG.baseUrl).replace(/\/$/, '')
}

