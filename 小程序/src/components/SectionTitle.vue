<template>
  <view class="section-title">
    <text>{{ title }}</text>
    <text v-if="moreText" class="more">{{ moreText }}</text>
  </view>
</template>
<script setup>
defineProps({ title: String, moreText: String })
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.section-title{display:flex;justify-content:space-between;align-items:center;margin:28rpx 0 16rpx;font-size:32rpx;font-weight:700}.more{color:$primary;font-size:24rpx}
</style>
