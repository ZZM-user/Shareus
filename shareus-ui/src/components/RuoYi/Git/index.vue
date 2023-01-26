<template>
  <div>
    <svg-icon icon-class="github"/>
  </div>
</template>

<script setup>
const url = ref('https://gitee.com/');

function goto() {
  window.open(url.value)
}
</script>
