<template>
  <text class="status" :class="type">{{ text }}</text>
</template>
<script setup>
defineProps({ text: String, type: { type: String, default: 'primary' } })
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.status{display:inline-flex;padding:8rpx 16rpx;border-radius:999rpx;font-size:22rpx}.primary{background:$primary-light;color:$primary}.accent{background:#fff2ea;color:$accent}.muted{background:#eef2ef;color:$text-secondary}
</style>
