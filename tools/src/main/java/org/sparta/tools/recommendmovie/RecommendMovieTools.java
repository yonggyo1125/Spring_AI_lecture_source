package org.sparta.tools.recommendmovie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RecommendMovieTools {

    @Tool(description = "사용자가 관람한 영화 목록을 제공합니다.")
    public List<String> getMovieListByUserId(@ToolParam(description = "사용자 ID", required = true) String userId) {
        // 데이터베이스에서 검색해서 가져온 내용
        return List.of(
                "엣지오브투모로우","투모로우","아이언맨","혹성탈출","타이나닉","엘리시움",
                "인터스텔라", "아바타", "마션"
        );
    }

    @Tool(description = "주어진 장르의 추천 영화 목록을 제공합니다.", returnDirect = true)
    public List<String> recommendMovie(@ToolParam(description = "장르", required = true) String genre) {
        //데이터베이스에서 검색해서 가져온 내용
        return List.of("크레이븐", "베놈", "메이드");
    }
}
