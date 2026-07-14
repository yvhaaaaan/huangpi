import { API_CONFIG, getApiBaseUrl } from '../config/api'
import { ApiEnvelope, ApiError, handleUnauthorized, request } from '../utils/request'

export interface UploadedFile {
  fileId: string
  url: string
}

const SESSION_KEY = 'auth.session.v2'

export const uploadImage = (filePath: string, businessType = 'merchant_product'): Promise<UploadedFile> => {
  const session = wx.getStorageSync(SESSION_KEY) as { token?: string } | undefined
  const token = session?.token || ''

  return new Promise<UploadedFile>((resolve, reject) => {
    wx.uploadFile({
      url: `${getApiBaseUrl()}/api/files`,
      filePath,
      name: 'file',
      formData: { businessType },
      header: token ? { Authorization: `Bearer ${token}` } : {},
      timeout: API_CONFIG.timeout,
      success: response => {
        try {
          const body = JSON.parse(response.data) as ApiEnvelope<UploadedFile>
          if (response.statusCode === 401 || body.code === 40100) {
            handleUnauthorized()
            reject(new ApiError(body.message || '登录状态已失效', body.code, response.statusCode, body.traceId))
            return
          }
          if (response.statusCode < 200 || response.statusCode >= 300 || body.code !== 0) {
            reject(new ApiError(body.message || '图片上传失败', body.code, response.statusCode, body.traceId))
            return
          }
          resolve(body.data)
        } catch (_error) {
          reject(new ApiError('图片上传返回格式错误'))
        }
      },
      fail: error => reject(new ApiError(error.errMsg || '图片上传失败')),
    })
  })
}

export const getFileInfo = (id: string): Promise<UploadedFile> => {
  return request<UploadedFile>({ path: `/api/files/${encodeURIComponent(id)}` })
}

export const deleteFile = (id: string): Promise<void> => {
  return request<void>({ path: `/api/files/${encodeURIComponent(id)}`, method: 'DELETE' })
}
