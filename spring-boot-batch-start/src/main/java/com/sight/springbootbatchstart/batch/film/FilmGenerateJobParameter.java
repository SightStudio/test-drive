package com.sight.springbootbatchstart.batch.film;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FilmGenerateJobParameter {
    private final Long filmCount;
}
