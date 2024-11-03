package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageAdService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;

    private final ImageAdService imageAdService;

    @Operation(summary = "Получение всех объявлений")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<AdsDto> findAllAds() {
        AdsDto adsDto = adService.getAllAds();
        return ResponseEntity.ok(adsDto);}


    @PostMapping
    @Operation(summary = "Добавление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<AdDto> createAd(@RequestPart AdDto properties,
                                          @RequestPart MultipartFile image,
                                          Authentication authentication) throws IOException {
        log.info("Вызван метод контроллера создание объявления");
        AdDto adDto = adService.saveAd(properties, image, authentication.getName());
        return ResponseEntity.ok(adDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ExtendedAdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ExtendedAdDto> getAdInfo(@PathVariable Integer id) {
        log.info("Вызван метод контроллера получение информации об объявлении");
        ExtendedAdDto extendedAdDto = adService.getAdInfo(id);
        return ResponseEntity.ok(extendedAdDto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adsService.isAuthorAd(principal.username, #adId)")
    @Operation(summary = "Удаление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity deleteAd(@PathVariable Integer id) {
        log.info("Вызван метод контроллера удаление объявления");
        adService.deleteAd(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adsService.isAuthorAd(principal.username, #adId)")
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<AdDto> updateAdInfo(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDto createOrUpdateAd) {
        log.info("Вызван метод контроллера обновление информации об объявлении");
        AdDto adDto = adService.updateInfoAd(id, createOrUpdateAd);
        return ResponseEntity.ok(adDto);
    }
    @Operation(summary = "информация об объявлениях")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    @GetMapping("/me")
    public ResponseEntity<AdsDto> findMyAds() {
        log.info("Вызван метод контроллера информация об объявлениях пользователя");
        AdsDto adsDto = adService.findMyAds();
        return ResponseEntity.ok(adsDto);
    }


    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @adsService.isAuthorAd(principal.username, #adId)")
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AdDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public void updateAdImage(@PathVariable Integer id, @RequestBody MultipartFile image) throws IOException {
        log.info("Вызван метод контроллера обновление информации об обявлении");
        imageAdService.updateAdImage(id, image);
    }

}