package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {
    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<AdsDTO> findAllAds() {
        return ResponseEntity.ok().build();
    }
    @PostMapping
    @Operation(summary = "Добавление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<AdDTO> createAd(@RequestBody AdDTO adDTO) {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ExtendedAdDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ExtendedAdDTO> getAdInfo(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
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
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @adsService.isAuthorAd(principal.username, #adId)")
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AdDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<AdDTO> updateAdInfo(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDTO createOrUpdateAd) {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<AdsDTO> findMyAds() {
        return ResponseEntity.ok().build();
    }
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole( 'ADMIN' ) or @adServiceImpl.findAdById(id).author.userName.equals(authentication.name)")
    public ResponseEntity updateAdImage(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }
}
