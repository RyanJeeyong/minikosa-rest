<!-- src/views/Search.vue -->
<template>
    <div id="wrapHome" class="search_wrap">
      <hr class="hide">
      <div class="wrap_search_header">
        <div class="wrap_search">
          <h2 class="screen_out">검색 키워드 입력 창</h2>
          <div class="search_form">
            <form @submit.prevent="performSearch">
              <input
                type="text"
                v-model="query"
                class="txt_search"
                id="txt_search"
                title="검색어입력"
                placeholder="검색어를 입력해 주세요."
                maxlength="20"
              />
              <input type="hidden" value="latest" name="sort" id="sortInput">
              <input type="hidden" value="store" name="type" id="typeInput">
              <button type="submit" class="btn btn-search">검색</button>
            </form>
          </div>
        </div>
      </div>
  
      <div id="searchWrap">
        <div class="search_header">
          <div class="tab_search">
            <ul id="contentsTab" class="inner_tab">
              <li :class="{ on: activeTab === 'store' }">
                <a href="#" @click.prevent="switchTab('store')" class="articleTab link_tab">식당</a>
              </li>
              <li :class="{ on: activeTab === 'review' }">
                <a href="#" @click.prevent="switchTab('review')" class="magazineTab link_tab">리뷰</a>
              </li>
            </ul>
          </div>
        </div>
  
        <!-- 식당 검색 결과 -->
        <div id="mArticle" v-show="activeTab === 'store'">
          <div id="resultArticle" class="search_result" style="margin-top: 0px;">
            <div class="result_search">
              <div class="search-result">
                <span>가게 검색 결과 : </span>
                <span>{{ storeResults.storeCount }}</span>
                <span> 건</span>
              </div>
              <div class="result-sort">
                <a href="#" @click.prevent="updateSort('latest', 'store')"><span :style="sortStyle('latest', 'store')">최신순</span></a>
                <a href="#" @click.prevent="updateSort('rating', 'store')"><span :style="sortStyle('rating', 'store')">평점순</span></a>
              </div>
            </div>
  
            <!-- 가게 검색 결과가 있을 때 -->
            <section class="food_section layout_padding-bottom" v-if="storeResults.storeCount > 0">
              <div class="container">
                <!-- 가게 검색 결과 -->
                <div class="filters-content">
                  <div class="row grid">
                    <div
                      v-for="store in storeResults.storeResults"
                      :key="store.storeId"
                      :class="'col-sm-6 col-lg-4 all ' + store.categoryName"
                    >
                      <div class="box">
                        <router-link :to="'/store/' + store.storeId">
                          <div class="img-box">
                            <img
                              :src="getImageUrl(store.storePhoto)"
                              alt="가게 사진"
                              @error="onImageError($event)"
                            />
                          </div>
                          <div class="detail-box">
                            <h5>{{ store.storeName }}</h5>
                            <div class="detail-desc-info">
                              <p>{{ store.storeDescription }}</p>
                              <div class="detail-rate">
                                <ul>
                                  <template v-if="store.ratingAvg !== null">
                                    <li>
                                      <img
                                        width="48"
                                        height="48"
                                        src="https://img.icons8.com/color/48/filled-star--v1.png"
                                        alt="filled-star--v1"
                                        class="icon_star"
                                      />
                                    </li>
                                    <li>
                                      <span class="rate_num">{{ store.ratingAvg }}</span>
                                    </li>
                                  </template>
                                  <li v-else>
                                    <span class="rate_num">아직 후기가 없습니다</span>
                                  </li>
                                </ul>
                              </div>
                            </div>
                          </div>
                        </router-link>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </section>
  
            <!-- 가게 검색 결과가 없을 때 -->
            <div class="no-results" v-else>
              <p>가게 검색 결과가 없습니다.</p>
            </div>
          </div>
        </div>
  
        <!-- 리뷰 검색 결과 -->
        <div id="mReview" v-show="activeTab === 'review'">
          <div id="resultReview" class="search_result" style="margin-top: 0px;">
            <div class="result_search">
              <div class="search-result">
                <span>리뷰 검색 결과 : </span>
                <span>{{ reviewResults.reviewCount }}</span>
                <span> 건</span>
              </div>
              <div class="result-sort">
                <a href="#" @click.prevent="updateSort('latest', 'review')"><span :style="sortStyle('latest', 'review')">최신순</span></a>
                <a href="#" @click.prevent="updateSort('rating', 'review')"><span :style="sortStyle('rating', 'review')">평점순</span></a>
              </div>
            </div>
  
            <!-- 리뷰 검색 결과가 있을 때 -->
            <div v-if="reviewResults.reviewCount > 0">
              <div v-for="review in reviewResults.reviewResults" :key="review.reviewId" class="review-box">
                <div class="review-header">
                  <div class="review-info">
                    <router-link :to="'/store/' + review.storeId" class="search_store_name">
                      {{ review.storeName }}
                    </router-link>
                    <div class="grade_star">
                      <img
                        width="48"
                        height="48"
                        src="https://img.icons8.com/color/48/filled-star--v1.png"
                        alt="filled-star--v1"
                        class="icon_star"
                      />
                      <em class="num_rate">{{ review.rating }}</em>
                      <span class="text_score">점</span>
                    </div>
                  </div>
                  <div class="review-actions">
                    <button v-if="isAdmin" @click="deleteReview(review.reviewId, review.memberId)" class="comment_user_button">삭제</button>
                    <router-link :to="'/store/' + review.storeId" class="comment_user_button">리뷰 보러가기</router-link>
                  </div>
                </div>
                <div class="comment_info">
                  <p class="text_comment">
                    <span>{{ review.reviewText }}</span>
                  </p>
                </div>
                <div class="unit_info">
                  <span class="text_item">작성자(닉네임) : </span>
                  <span class="text_username">{{ review.memberNickname }}</span>
                  <span class="bar"></span>
                  <span class="text_item">별점 : </span>
                  <span class="text_desc">{{ review.rating }}</span>
                  <span class="bar"></span>
                  <span class="text_item">작성일 : </span>
                  <span class="time_write">{{ formatDate(review.createdAt) }}</span>
                </div>
              </div>
            </div>
  
            <!-- 리뷰 검색 결과가 없을 때 -->
            <div class="no-results" v-else>
              <p>리뷰 검색 결과가 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
  
      <!-- JavaScript -->
      <!-- 필요한 스크립트는 Vue 컴포넌트 내부에서 처리 -->
    </div>
  </template>
  
  <script>
  import api from '../axios.js'
  import { ref, reactive, onMounted } from 'vue'
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Search',
    setup() {
      const query = ref('')
      const sort = ref('latest')
      const type = ref('store')
      const activeTab = ref('store')
      const storeResults = reactive({
        query: '',
        storeResults: [],
        storeCount: 0
      })
      const reviewResults = reactive({
        query: '',
        reviewResults: [],
        reviewCount: 0
      })
      const authStore = useAuthStore()
      const isAdmin = ref(false)
  
      onMounted(() => {
        // URL에서 파라미터 가져오기
        const urlParams = new URLSearchParams(window.location.search)
        query.value = urlParams.get('q') || ''
        sort.value = urlParams.get('sort') || 'latest'
        type.value = urlParams.get('type') || 'store'
        activeTab.value = type.value
        performSearch()
        if (authStore.user && authStore.user.roleId === 2) {
          isAdmin.value = true
        }
      })
  
      const performSearch = async () => {
  
          const response = await api.get('/search', {
            params: {
              q: query.value || '',
              sort: sort.value,
              type: type.value
            }
          })
  
          if (type.value === 'store') {
            Object.assign(storeResults, response.data)
          } else if (type.value === 'review') {
            Object.assign(reviewResults, response.data)
          }
      }
  
      const switchTab = (tab) => {
        activeTab.value = tab
        type.value = tab
        performSearch()
      }
  
      const updateSort = (newSort, newType) => {
        sort.value = newSort
        type.value = newType
        performSearch()
      }
  
      const sortStyle = (currentSort, currentType) => {
        return {
          fontWeight: sort.value === currentSort && type.value === currentType ? 'bold' : 'normal',
          color: sort.value === currentSort && type.value === currentType ? '#000000' : ''
        }
      }
  
      const onImageError = (event) => {
        event.target.onerror = null
        event.target.src = '/images/main_logo.png'
      }
  
      const formatDate = (dateString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
        return new Date(dateString).toLocaleDateString('ko-KR', options)
      }
  
      const deleteReview = async (reviewId, memberId) => {
        if (confirm('해당 리뷰를 삭제하시겠습니까?')) {
          try {
            await api.delete(`/store/${memberId}/review/${reviewId}`)
            alert('리뷰가 삭제되었습니다.')
            performSearch()
          } catch (error) {
            console.error(error)
            alert('리뷰 삭제 중 오류가 발생했습니다.')
          }
        }
      }
  
      // 이미지 URL 생성 함수 추가
      const getImageUrl = (path) => {
        const serverHost = 'http://localhost:8090' // 백엔드 서버 주소
        return serverHost + path
      }
  
      return {
        query,
        sort,
        type,
        activeTab,
        storeResults,
        reviewResults,
        performSearch,
        switchTab,
        updateSort,
        sortStyle,
        isAdmin,
        onImageError,
        formatDate,
        deleteReview,
        getImageUrl, // 함수 반환에 추가
      }
    }
  }
  </script>
  

<style scoped>
/* 기본 리셋 및 스타일 */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: "맑은 고딕", sans-serif;
    background-color: #f9f9f9;
    color: #333;
    line-height: 1.6;
}

/* 헤더 및 내비게이션 바 전체 너비 설정 */
.header {
    width: 100%;
    background-color: #fff;
    /* 배경색 */
    border-bottom: 2px solid #FF885B;
    /* 테두리 추가 및 색상 변경 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    /* 헤더에 그림자 추가 */
    /* 고정 헤더 position: fixed;  */
    top: 0;
    left: 0;
    z-index: 600;
}

/* 컨테이너 스타일 */
.container {
    /*  width: 1100px;*/
    /*
    margin: 80px 0px 0 -100px;
    padding: 20px;
*/
}

/* 헤더 내부 컨텐츠 정렬 */
.header .container-inner {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px;
}

/* 로고 스타일 */
.header .logo {
    display: flex;
    align-items: center;
    margin-left: 20px;
}

.header .logo img {
    width: 120px;
    height: auto;
    margin-right: 5px;
}

.header .logo h1 {
    font-size: 24px;
    color: #FF885B;
}

/* 내비게이션 스타일 */
.nav {
    display: flex;
    gap: 25px;
    position: relative;
    align-items: center;
    margin-right: 30px;
}

.nav a {
    text-decoration: none;
    color: #333;
    font-weight: bold;
    transition: color 0.5s;
    padding: 8px 12px;
}

.nav a:hover {
    color: #FF885B;
}

/* 드롭다운 메뉴 스타일 */
.dropdown {
    position: relative;
    display: inline-block;
}

.dropdown button {
    background: none;
    border: none;
    cursor: pointer;
    font-size: 16px;
    display: flex;
    align-items: center;
    color: #333;
    font-weight: bold;
}

.dropdown button:hover {
    color: #FF885B;
}

.dropdown-content {
    display: none;
    position: absolute;
    right: 0;
    background-color: #fff;
    min-width: 160px;
    box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
    border: 1px solid #ddd;
    z-index: 1;
    border-radius: 5px;
}

.dropdown-content a {
    color: #333;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    transition: background-color 0.3s;
}

.dropdown-content a:hover {
    background-color: #f1f1f1;
    color: #FF885B;
}

.dropdown:hover .dropdown-content {
    display: block;
}

/* 푸터 스타일 */
footer {
    background-color: #FF885B;
    color: #fff;
    padding: 20px 10px;
    margin-top: 40px;
}

.footer-container {
    max-width: 1500px;
    height: 80%;
    width: 100%;
    /* 전체 너비 */
    margin: 0 auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 100px;
    padding: 10px 0px;
}

.footer-section {
    flex: 1 1 200px;
}

.footer-section h4 {
    margin-bottom: 15px;
    font-size: 18px;
    border-bottom: 2px solid #fff;
    display: inline-block;
    padding-bottom: 5px;
}

.footer-section ul {
    list-style: none;
}

.footer-section ul li {
    margin-bottom: 10px;
}

.footer-section ul li a {
    color: #fff;
    text-decoration: none;
    transition: color 0.3s;
}

.footer-section ul li a:hover {
    color: #e07a4d;
}

.social-icons {
    display: flex;
    gap: 10px;
}

.social-icons a img {
    width: 24px;
    height: 24px;
    filter: invert(100%);
    transition: filter 0.3s;
}

.social-icons a:hover img {
    filter: invert(75%) sepia(100%) saturate(500%) hue-rotate(300deg);
}

.footer-bottom {
    text-align: center;
    font-size: 14px;
    border-top: 1px solid rgba(255, 255, 255, 0.3);
    padding-top: 10px;
}

/* 검색바 부분 */
.wrap_search_header {
    background-color: #fff;
}

.wrap_search {
    background-color: #fff;
    margin: 0 auto;
    overflow: hidden;
    padding: 63px 0 1px;
    position: relative;
    width: 1100px;
}

.search_header {
    background-color: #fff;
    border-bottom: 1px solid #ddd;
}

.search_form {
    text-align: center;
    margin-bottom: 20px;
}

.search_form .txt_search {
    width: 600px;
    height: 50px;
    padding: 0 20px;
    font-size: 16px;
    border: 2px solid #FF885B;
    border-radius: 25px 0 0 25px;
    outline: none;
    background-color: #fff;
}

.search_form .txt_search::placeholder {
    color: #999;
}

.search_form .btn-search {
    height: 50px;
    padding: 0 30px;
    font-size: 16px;
    color: #fff;
    background-color: #FF885B;
    border: none;
    border-radius: 0 25px 25px 0;
    cursor: pointer;
    transition: background-color 0.3s;
}

.search_form .btn-search:hover {
    background-color: #e07a4d;
}

body,
button,
input,
select,
td,
textarea,
th {
    font-size: 14px;
    line-height: 1.5;
}

button,
input,
select,
textarea {
    vertical-align: middle;
}

blockquote,
body,
button,
code,
dd,
div,
dl,
dt,
fieldset,
form,
h1,
h2,
h3,
h4,
h5,
h6,
input,
legend,
li,
ol,
p,
pre,
select,
td,
textarea,
th,
ul {
    -webkit-font-smoothing: antialiased;
    -moz-font-smoothing: unset;
    -moz-osx-font-smoothing: grayscale;
    font-smoothing: antialiased;
    -webkit-backface-visibility: hidden;
    -moz-backface-visibility: hidden;
    backface-visibility: hidden;
    font-family: "Noto Sans DemiLight", "Malgun Gothic", sans-serif;
    /*margin: 0;*/
    padding: 0;
    text-rendering: inherit;
}

.hide {
    display: none;
}

.ir_caption,
.screen_out {
    line-height: 0;
    overflow: hidden;
    text-indent: -9999px;
}

/* 검색 카테고리 */
.search-result {
    color: #666;
    font-size: 16px;
    margin: 5px 0px 5px 20px;
    display: inline;
}

.result-sort {
    display: inline-block;
    float: right;
}

.result-sort span {
    color: #666;
    margin-left: 20px;
    margin-right: 5px;
    font-size: 16px;
}

.result-sort span:active {
    color: #333;
    font-weight: bold;
}

.search_header {
    background-color: #fff;
    border-bottom: 1px solid #ddd;
}

.tab_search {
    margin: 0 auto;
    position: relative;
    width: 1100px;
    z-index: 9999;
}

.tab_search .inner_tab {
    height: 51px;
}

.tab_search .inner_tab li {
    float: left;
    font-family: "Noto Sans Light";
}

.tab_search .inner_tab .on .link_tab {
    border-bottom: 2px solid #FF885B;
    color: #FF885B;
}

.tab_search .inner_tab .link_tab {
    color: #666;
    float: left;
    font-size: 14px;
    letter-spacing: -1px;
    margin-right: 6px;
    padding: 19px 0 10px;
    width: 80px;
}

a,
a:active,
a:hover {
    text-decoration: none;
    color: #333;
}

dl,
li,
menu,
ol,
ul {
    list-style: none;
}

/* 검색 결과 */
body {
    background: #fff;
    color: #333;
}

.search_result {
    margin: 0 auto;
    overflow: hidden;
}

/* 식당 섹션 */
.food_section {
    margin: 0 auto;
    width: 1200px;
}

.layout_padding2-bottom {
    padding-bottom: 75px;
}

.food_section .filters_menu {
    padding: 0;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    list-style-type: none;
    margin: 45px 0 20px 0;
}

.food_section .filters_menu li {
    padding: 7px 25px;
    cursor: pointer;
    border-radius: 25px;
    color: #999;
}

.food_section .filters_menu li:hover {
    color: #000;
}

.food_section .filters_menu li.active {
    background-color: #FF885B;
    color: #ffffff;
}

.food_section .box {
    position: relative;
    margin-top: 25px;
    background-color: #ffffff;
    border-radius: 10px;
    color: #FF885B;
    border-radius: 15px;
    overflow: hidden;
    /* background: linear-gradient(to bottom, #00BCD4 25px, #FF885B 25px); */
    border: 1px solid #e7e7e7;
}

.food_section .box .img-box {
    background: #f1f2f3;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    height: 215px;
    border-radius: 0 0 45px 45px;
    margin: -1px;
    padding: 25px;
}

.food_section .box .img-box img {
    max-width: 100%;
    max-height: 145px;
    -webkit-transition: all .2s;
    transition: all .2s;
}

.detail-desc-info p {
    height: 85px;
    /*크게 볼 때 메뉴 크기*/
}

.detail-rate span {
    float: right;
    font-size: 18px;
    font-weight: 600;
}

.icon_star {
    margin-top: -7px;
    width: 30%;
    height: 30%;
}

.food_section .box .detail-box {
    padding: 25px 25px 0px 25px;
    max-height: 205px;
    margin-bottom: 20px;
}

.food_section .box .detail-box h5 {
    font-weight: 600;
    color: black;
}

.food_section .box .detail-box p {
    font-size: 14px;
    color: #666;
    overflow: hidden;
}

.food_section .box .detail-box h6 {
    margin-top: 10px;
}

.food_section .box .options {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
}

.food_section .box .options a {
    width: 40px;
    height: 40px;
    border-radius: 100%;
    background: #ffbe33;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
}

.food_section .box .options a svg {
    width: 18px;
    height: auto;
    fill: #ffffff;
}

.food_section .box:hover .img-box img {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
}

li {
    list-style-type: none;
}

.detail-desc-info li span {
    margin-bottom: 15px;
}

.detail-desc-info ul {
    display: ruby;
    float: right;
}

@media (min-width: 768px) {
    .col-md-4 {
        -ms-flex: 0 0 33.333333%;
        /* flex: 0 0 33.333333%; */
        max-width: 33.333333%;
    }
}

/* 댓글 */
.comments_box {
    border: 1px solid #eaeaea;
    margin-top: 10px;
    padding: 29px 79px 31px;
    background: #fff;
}

.comments_box .total_comment {
    display: block;
    font-size: 17px;
    letter-spacing: -2px;
    line-height: 24px;
    margin-top: 20px;
    position: relative;
}

.comments_box .total_comment .color_b {
    display: inline-block;
    font-size: 18px;
    font-weight: 400;
    height: 20px;
    letter-spacing: 0;
    line-height: 28px;
    padding-left: 6px;
    vertical-align: top;
}

.color_b {
    color: #FF885B;
}

.user_grade_box {
    overflow: hidden;
    padding: 11px 0 0;
}

.user_grade_box .num_rate {
    margin-top: 3px;
    margin-left: 10px;
    float: left;
    font-size: 28px;
    height: 30px;
    letter-spacing: 0;
    line-height: 30px;
    color: #FF885B;
}

.user_grade_box .text_score {
    display: inline-block;
    font-size: 25px;
    line-height: 33px;
    margin-left: 4px;
    vertical-align: top;
    color: #000000;
}

.view_like_point {
    margin: 0 -2px;
    max-height: 80px;
    overflow: hidden;
    padding-top: 13px;
}

.view_like_point .chip_like_point {
    border-radius: 30px;
    color: #ff885b;
    float: left;
    font-weight: 700;
    height: 36px;
    line-height: 36px;
    margin: 4px 2px 0;
    padding: 0 19px 0 20px;
}

.view_like_point .chip_like_point .chip_type1 {
    background-color: aqua;
}

/* 리뷰 박스 전체 스타일 */
.review-box {
    border: 1px solid #ddd;
    padding: 15px;
    margin-top: 20px;
    margin-bottom: 24px;
    background-color: #fff;
    border-radius: 5px;
    position: relative;
}

/* 리뷰 헤더: 정보와 버튼을 가로로 배치 */
.review-header {
    display: flex;
    justify-content: space-between; /* 좌우 간격을 최대화 */
    align-items: flex-start; /* 수직 정렬 */
    margin-bottom: 10px;
}

/* 리뷰 정보: 가게명과 별 평점을 좌측에 정렬 */
.review-info {
    display: flex;
    align-items: center;
    gap: 10px; /* 가게명과 별 평점 사이 간격 */
}

/* 가게명 스타일 */
.search_store_name {
    font-size: 20px;
    color: #FF885B;
    text-decoration: none;
}

.search_store_name:hover {
    text-decoration: underline;
}

/* 별 평점 스타일 */
.grade_star {
    display: flex;
    align-items: center;
}

.grade_star img.icon_star {
    width: 25px;
    height: 25px;
    margin-right: 5px;
}

.num_rate {
    font-size: 20px;
    font-weight: bold;
    margin-right: 5px;
    color: #FF885B;
}

.text_score {
    font-size: 20px;
    color: #555;
}

/* 검색 결과 없음 메시지 스타일 */
.no-results {
    text-align: center;
    padding: 50px 0;
    font-size: 18px;
    color: #666;
}

/* 버튼 그룹 스타일 */
.review-actions {
    display: flex;
    gap: 10px; /* 버튼 간 간격 */
}

/* 버튼 스타일 */
.comment_user_button {
    background: none;
    border: none;
    color: #FF885B;
    cursor: pointer;
    font-size: 14px;
    padding: 5px 10px;
    transition: color 0.3s;
}

.comment_user_button:hover {
    text-decoration: underline;
}

.text_item {
    font-weight: bold;
}

.review_list .reply_comment_list {
    padding-top: 10px;
}

.reply_comment_list {
    background-color: #e7e7e7;
}

.ico_comm .ico_comm_fgm {
    font-size: 1px;
    line-height: 0;
    overflow: hidden;
    text-indent: -9999px;
}

.ico_comm {
    background-repeat: no-repeat;
}

.view_like_point .chip_like_point .ico_like3 {
    display: inline-block;
    height: 13px;
    margin: 10px 5px 0 0;
    vertical-align: top;
    width: 13px;
}

.review_box {
    /*padding-top: 20px; */
    /*태그 넣을 때 padding 확장필요*/
    padding-top: 10px;
}

.review_list > li {
    border-top: 1px solid #f2f2f2;
    font-size: 0;
    min-height: 52px;
    padding: 30px 0 18px 79px;
    position: relative;
}

.review_list .profile_info {
    background-image: url(image/%EB%B3%B4%EB%85%B8%EB%B3%B4%EB%85%B8.png);
    /*108x108의 기본 프로필 이미지 필요*/
    background-size: 48px 48px;
}

.review_list .reply_img {
    background-size: 100% 100%;
    height: 48px;
    left: 0;
    position: absolute;
    top: 10px;
    width: 48px;
    margin-left: 25px;
}

.review_list .thumb_g {
    display: block;
    height: 48px;
    width: 48px;
}

.review_list .reply_img .thumb_g {
    display: block;
    height: 24px;
    width: 24px;
}

.review_list .profile_info {
    background: url(//t1.daumcdn.net/localimg/localimages/07/2018/pc/bg_profile.png) no-repeat;
    background-size: 100% 100%;
    height: 48px;
    left: 0;
    position: absolute;
    top: 30px;
    width: 48px;
}

.reply_comment_list .unit_info {
    overflow: hidden;
    margin-bottom: 28px;
    margin-left: 100px;
}

.review_list .unit_info {
    overflow: hidden;
    margin-bottom: 28px;
    /*프로필 사진 넣을 시 아래 margin 지울 것*/
    margin-left: -80px;
}

.review_list .link_user {
    float: left;
}

.review_list .inner_user {
    display: flex;
}

.review_list .link_user .text_username {
    color: #333;
    font-size: 14px;
    font-weight: 700;
    max-width: 158px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.review_list .bar {
    background: #e2e2e2;
    height: 11px;
    margin: 6px 6px 0;
}

.review_list .unit_info .text_desc {
    color: #666;
    /* float: left; */
    font-size: 14px;
    font-weight: 700;
    padding-left: 3px;
}

.review_list .unit_info .time_write {
    color: #959595;
    font-size: 14px;
}

.text_item {
    padding-left: 6px;
}

.review_list .unit_info .text_item {
    color: #666;
    /* float: left; */
    font-size: 14px;
}

.review_list .time_write {
    /* float: left; */
    letter-spacing: 0;
    line-height: 21px;
}

.review_list .photo_group {
    padding-top: 14px;
}

.review_list .photo_group .list_photo {
    font-size: 0;
    white-space: nowrap;
}

.review_list .photo_group .list_photo li {
    border-top: 0;
    display: inline-block;
    padding: 0;
}

.review_list .photo_group .box_photo {
    display: block;
    height: 139px;
    position: relative;
    width: 139px;
}

.review_list .photo_group .img_thumb {
    height: 100%;
}

.img_thumb {
    vertical-align: top;
    width: 100%;
}

.icon_reply {
    margin-top: 20px;
}

.review_list .comment_info {
    margin: 20px 0;
    overflow: hidden;
    padding: 9px 12px 0 0;
}

.review_list .text_comment {
    margin: 10px 0;
    color: #444;
    line-height: 23px;
    overflow: hidden;
}

.review_list .text_comment span {
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;
    display: block;
    display: -webkit-box;
    font-size: 25px;
    line-height: 22px;
    max-height: 88px;
    overflow: hidden;
    white-space: pre-wrap;
    word-break: break-all;
}

.comment_info_reply {
    margin-top: 20px;
    margin-right: 20px;
}

.review_list .reply_comment_list .wrap_util {
    right: 0;
    top: 10px;
}

.review_list .wrap_util {
    right: 0;
    top: 30px;
}

.wrap_util {
    position: absolute;
}

.ico_comm {
    background-image: url(//t1.daumcdn.net/localimg/localimages/07/2018/pc/rtn/ico_detail_240813.png);
    background-size: 270px auto;
}

.ico_comm,
.ico_comm_fgm {
    display: block;
    font-size: 1px;
    line-height: 0;
    overflow: hidden;
    text-indent: -9999px;
}

.wrap_util .list_opt li {
    border-top: 0;
    min-height: auto;
    padding: 0;
}

.wrap_util .link_util {
    color: #444;
    display: block;
    font-size: 13px;
    line-height: 30px;
    text-decoration: none;
}

.review_list .layer_util .link_util {
    text-align: center;
}

.link_more {
    border-top: 1px solid #f2f2f2;
    display: block;
    height: 56px;
    line-height: 57px;
    margin-top: 16px;
    text-align: center;
}

.link_more .ico_more {
    background-position: -160px -60px;
    display: inline-block;
    height: 5px;
    margin: 25px 0 0 6px;
    vertical-align: top;
    width: 10px;
}

.comment_user_button {
    padding-left: 15px;
    color: #959595;
    text-decoration: none;
}

.comment_user_button:hover {
    text-decoration: none;
}

/* 식당 및 리뷰 섹션 폭 조정 */
#mArticle,
#mReview {
    margin: 0 auto;
    padding-top: 16px;
    width: 1200px;
}

a {
    text-decoration: none;
    color: black;
}

.link_more {
    border-top: 1px solid #f2f2f2;
    display: block;
    height: 56px;
    line-height: 57px;
    margin-top: 16px;
    text-align: center;
}

.link_more:hover {
    color: #FF885B;
}

button {
    border: none;
    background-color: white;
}

.comment_user_button {
    padding-left: 15px;
    color: #959595;
}

.comment_user_button:hover {
    text-decoration: underline;
    text-underline-offset: 5px;
}

.result_article .btn-box {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    margin-top: 45px;
}

.result_article .btn-box a {
    display: inline-block;
    padding: 10px 55px;
    color: #666;
    border-radius: 45px;
    -webkit-transition: all 0.3s;
    transition: all 0.3s;
    border: 1px solid #e7e7e7;
}

.result_article .btn-box a:hover {
    background-color: #FF885B;
    color: white;
    border: none;
}

em {
    font-style: normal;
}

.search_store_name {
    font-weight: 500;
    font-size: 24px;
    float: left;
}

.search_store_name:hover {
    color: #FF885B;
}

.grade_star .icon_star {
    width: 3%;
    margin-top: 4px;
    float: left;
    margin-left: 10px;
}

/* 반응형 디자인 (간단히 추가) */
@media (max-width: 850px) {
    .food_section,
    #mArticle,
    #mReview {
        width: 90%;
    }

    .search-input-group .txt_search {
        width: 70%;
    }

    .search-input-group .btn-search {
        padding: 0 15px;
    }
}

</style>
