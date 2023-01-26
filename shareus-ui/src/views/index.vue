<template>
  <div id="line" class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import * as echarts from "echarts";

const countries = [
  'Finland'
];
const datasetWithFilters = [];
const seriesList = [];
echarts.util.each(countries, function (country) {
  const datasetId = 'dataset_' + country;
  datasetWithFilters.push({
    id: datasetId,
    fromDatasetId: 'dataset_raw',
    transform: {
      type: 'filter',
      config: {
        and: [
          {dimension: 'Year', gte: 1950},
          {dimension: 'Country', '=': country}
        ]
      }
    }
  });
  seriesList.push({
    type: 'line',
    datasetId: datasetId,
    showSymbol: false,
    name: country,
    endLabel: {
      show: true,
      formatter: function (params) {
        return params.value[3] + ': ' + params.value[0];
      }
    },
    labelLayout: {
      moveOverlap: 'shiftY'
    },
    emphasis: {
      focus: 'series'
    },
    encode: {
      x: 'Year',
      y: 'Income',
      label: ['Country', 'Income'],
      itemName: 'Year',
      tooltip: ['Income']
    }
  });
});
var option = {
  animationDuration: 10000,
  dataset: [
    {
      id: 'dataset_raw',
      source: [[
        "Income",
        "Life Expectancy",
        "Population",
        "Country",
        "Year"
      ],
        [
          815,
          34.05,
          351014,
          "Australia",
          1800
        ],
        [
          1314,
          39,
          645526,
          "Canada",
          1800
        ],
        [
          985,
          32,
          321675013,
          "China",
          1800
        ],
        [
          864,
          32.2,
          345043,
          "Cuba",
          1800
        ],
        [
          1244,
          36.5731262,
          977662,
          "Finland",
          1800
        ]
      ]
    },
    ...datasetWithFilters
  ],
  title: {
    text: 'Income of Germany and France since 1950'
  },
  tooltip: {
    order: 'valueDesc',
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    nameLocation: 'middle'
  },
  yAxis: {
    name: 'Income'
  },
  grid: {
    right: 140
  },
  series: seriesList
};
let dom = document.getElementById("app");
// echarts.init(dom).setOption(option);
</script>

<style scoped lang="scss">

</style>

