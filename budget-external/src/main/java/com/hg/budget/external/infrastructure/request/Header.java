package com.hg.budget.external.infrastructure.request;

public record Header(String title, String subtitle, String imageUrl, ImageType imageType) {

    public static Header of(String title, String subtitle) {
        return new Header(title, subtitle, "https://developers.google.com/chat/images/quickstart-app-avatar.png", ImageType.CIRCLE);
    }
}
