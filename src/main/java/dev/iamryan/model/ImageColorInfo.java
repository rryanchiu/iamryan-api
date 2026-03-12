package dev.iamryan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageColorInfo {
    private RgbColor domainColor;
    private RgbColor secondColor;
    private RgbColor thirdColor;
    private List<RgbColor> colors;

    public String getColorsString() {
        if (CollectionUtils.isEmpty(colors)) {
            return "";
        }
        return colors.stream().map(RgbColor::toHex).collect(Collectors.joining(","));
    }

    @Override
    public String toString() {
        return "ImageColorInfo{" +
                "domainColor=" + domainColor.toHex() +
                ", secondColor=" + secondColor.toHex() +
                ", thirdColor=" + thirdColor.toHex() +
                ", colors=" + getColorsString() +
                '}';
    }
}
