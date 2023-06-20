import { createGlobalStyle } from "styled-components";

const GlobalStyles = createGlobalStyle`
  :root {
    // 테마 컬러
    --first-theme-color: #606C5D;
    --second-theme-color: #FFF4F4;
    --third-theme-color: #F7E6C4;
    --fourth-theme-color: #F1C376;
    
    // 기본 컬러
    --font-color: #0C0A09;
    --link-color: #0074cc;
    --error-color: #F45050;
  }

  * {
    // 레이아웃 리셋
    box-sizing: border-box;
    // 웹 폰트 적용
    font-family: 'SUITE-Regular';
  }
  
  @font-face {
    font-family: 'SUITE-Regular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2304-2@1.0/SUITE-Regular.woff2') format('woff2');
    font-weight: 400;
    font-style: normal;
  }

  body {
    // 레이아웃 리셋
    margin: 0;
    padding: 0;
  }

  html,
  body {
    height: 100%;
  }

  img,
  picture,
  video,
  canvas,
  svg {
    display: block;
    max-width: 100%;
  }

  ol, ul {
    list-style: none;
  }

  button {
    border: 0;
    cursor: pointer;
  }
`;

export default GlobalStyles;
