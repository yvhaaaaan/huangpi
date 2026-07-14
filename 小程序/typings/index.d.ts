/// <reference path="./types/index.d.ts" />

interface IAppOption {
  globalData: {
    session: import('../miniprogram/utils/auth').AuthSession | null,
  }
}
