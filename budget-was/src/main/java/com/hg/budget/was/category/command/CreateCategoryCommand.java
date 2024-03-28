package com.hg.budget.was.category.command;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryCommand(@NotBlank String name) {

}
