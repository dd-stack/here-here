import { createGlobalStyle } from "styled-components";

const GlobalStyles = createGlobalStyle`
  :root {
    // 테마 컬러
    --first-theme-color: #606C5D;
    --second-theme-color: #FFF4F4;
    --third-theme-color: #F7E6C4;
    --fourth-theme-color: #F1C376;
    
    // 기본 컬러
    --background-color: #fdfcfa;
    --font-color: #0C0A09;
    --link-color: #0074cc;
    --error-color: #F45050;
  }

  // 웹 폰트 적용
  @font-face {
    font-family: 'Pretendard-Regular';
    src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff') format('woff');
    font-weight: 400;
    font-style: normal;
  }

  @font-face {
    font-family: 'KyoboHand';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@1.0/KyoboHand.woff') format('woff');
    font-weight: normal;
    font-style: normal;
  }

  * {
    font-family: 'Pretendard-Regular';
    color: var(--font-color);
    // 레이아웃 리셋
    box-sizing: border-box;
  }

  html, body {
    height: 100%;
  }

  body {
    margin: 0 auto;
    padding: 0;
    max-width: 640px;
    box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
  }

  img, picture, video, canvas, svg {
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
