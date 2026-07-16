/// <reference path="./types/index.d.ts" />

interface IAppOption {
  globalData: {
    session: import('../miniprogram-src/utils/auth').AuthSession | null,
  }
}
