import { useEffect, useRef } from "react";

export default function KakaoMap() {
  const mapContainerRef = useRef(null);

  useEffect(() => {
    const mapOptions = {
      center: new window.kakao.maps.LatLng(33.450701, 126.570667),
      level: 3,
    };

    // 지도를 생성합니다
    const map = new window.kakao.maps.Map(mapContainerRef.current, mapOptions);

    // 주소-좌표 변환 객체를 생성합니다
    const geocoder = new window.kakao.maps.services.Geocoder();

    // 주소로 좌표를 검색합니다
    geocoder.addressSearch("제주특별자치도 제주시 첨단로 242", function (result, status) {
      // 정상적으로 검색이 완료됐으면
      if (status === window.kakao.maps.services.Status.OK) {
        const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시합니다
        const marker = new window.kakao.maps.Marker({
          map: map,
          position: coords,
        });

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
      }
    });
  }, []);
  return (
    <div>
      <div id="map" style={{ width: "500px", height: "400px" }} ref={mapContainerRef} />
    </div>
  );
}
