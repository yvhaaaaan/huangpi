<template>
  <view class="app-root">
    <slot />
  </view>
</template>

<style>
page {
  margin: 0;
  background: #f7f4ee;
  color: #2b2b2b;
  font-size: 28rpx;
}

html,
body,
#app {
  width: 100%;
  height: 100%;
}

.app-root {
  min-height: 100vh;
}
</style>

