package com.gc_demo.cms;

import com.gc_demo.cms.web.DummyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocateService {

    private static final Logger LOG = LoggerFactory.getLogger(AllocateService.class);
    private static final int SIZE_50_MB = 50 * 1024 * 1024;
    private static final int SIZE_100_MB = 100 * 1024 * 1024;

    public void allocateBigObject() {

        // 큰 배열 생성 (100MB 크기의 int 배열)
        int size = SIZE_100_MB; // 100MB
        int[] largeArray = new int[size];

        // 배열을 초기화해서 메모리를 실제로 사용하게 만든다
        for (int i = 0; i < size; i++) {
            largeArray[i] = i;
        }

        LOG.info("100MB 크기의 배열이 생성되었습니다!");

        // Garbage Collection 테스트를 위해 계속 큰 객체를 생성
        for (int i = 0; i < 10; i++) {
            createLargeObject();
            LOG.info("{}번째 큰 객체가 생성되었습니다!", i + 1);
        }
    }

    private void createLargeObject() {
        // 큰 문자열 생성 (50MB 크기)
        StringBuilder sb = new StringBuilder(SIZE_50_MB); // 50MB
        for (int i = 0; i < SIZE_50_MB; i++) {
            sb.append('a');
        }

        String largeString = sb.toString();

        // 큰 문자열 사용
        LOG.info("50MB 크기의 문자열이 생성되었습니다!");
    }

    public void createBatchObjects(int batchSize) {
        // 객체를 생성하고 참조 해제
        final List<DummyObject> objects = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            objects.add(new DummyObject(i));
        }
        objects.clear(); // 참조 해제
    }
}
