import { useEffect, useRef } from "react";

export default function KakaoMap({ location }) {
  const mapContainerRef = useRef(null);

  useEffect(() => {
    const mapOptions = {
      center: new window.kakao.maps.LatLng(33.450701, 126.570667),
      level: 3,
    };

    // 지도 생성
    const map = new window.kakao.maps.Map(mapContainerRef.current, mapOptions);

    // 주소 - 좌표 변환 객체 생성
    const geocoder = new window.kakao.maps.services.Geocoder();

    // 주소로 좌표 검색
    geocoder.addressSearch(`${location}`, function (result, status) {
      // 정상적으로 검색이 완료됐으면,
      if (status === window.kakao.maps.services.Status.OK) {
        const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시한다.
        const marker = new window.kakao.maps.Marker({
          map: map,
          position: coords,
        });

        // 지도 중심 이동 (결과값으로 받은 위치로)
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
