const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/api", //proxy가 필요한 path prameter를 입력
    createProxyMiddleware({
      target: `${process.env.REACT_APP_BASE_URL}`, //타겟이 되는 api url
      changeOrigin: true, //대상 서버 구성에 따라 호스트 헤더가 변경되도록 설정하는 부분
    })
  );
  app.use(
    "/image",
    createProxyMiddleware({
      target: `${process.env.REACT_APP_BASE_URL}`,
      changeOrigin: true,
    })
  );
  app.use(
    "/making",
    createProxyMiddleware({
      target: `${process.env.REACT_APP_BASE_URL}`,
      changeOrigin: true,
    })
  );
  app.use(
    "/member",
    createProxyMiddleware({
      target: `${process.env.REACT_APP_BASE_URL}`,
      changeOrigin: true,
    })
  );
  app.use(
    "/invitation",
    createProxyMiddleware({
      target: `${process.env.REACT_APP_BASE_URL}`,
      changeOrigin: true,
    })
  );
  app.use(
    "/calendar",
    createProxyMiddleware({
      target: `${process.env.REACT_APP_BASE_URL}`,
      changeOrigin: true,
    })
  );
};
