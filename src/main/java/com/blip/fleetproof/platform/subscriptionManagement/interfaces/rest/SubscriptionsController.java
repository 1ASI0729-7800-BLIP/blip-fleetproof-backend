package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetAllSubscriptionsQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetSubscriptionByIdQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetSubscriptionsByUserIdQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.services.SubscriptionCommandService;
import com.blip.fleetproof.platform.subscriptionManagement.domain.services.SubscriptionQueryService;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources.CreateSubscriptionResource;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources.SubscriptionResource;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources.UpdateSubscriptionResource;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.transform.CreateSubscriptionCommandFromResourceAssembler;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.transform.UpdateSubscriptionCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Monitoring plan subscriptions")
public class SubscriptionsController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionsController(SubscriptionCommandService subscriptionCommandService,
                                   SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @GetMapping
    @Operation(summary = "List subscriptions, optionally filtered by userId")
    public List<SubscriptionResource> getSubscriptions(@RequestParam(required = false) Long userId) {
        var subscriptions = userId == null
                ? subscriptionQueryService.handle(new GetAllSubscriptionsQuery())
                : subscriptionQueryService.handle(new GetSubscriptionsByUserIdQuery(userId));
        return subscriptions.stream().map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Get a subscription by id")
    public ResponseEntity<SubscriptionResource> getSubscriptionById(@PathVariable Long subscriptionId) {
        return subscriptionQueryService.handle(new GetSubscriptionByIdQuery(subscriptionId))
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a subscription")
    public ResponseEntity<SubscriptionResource> createSubscription(@RequestBody CreateSubscriptionResource resource) {
        var command = CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var subscription = subscriptionCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription));
    }

    @PutMapping("/{subscriptionId}")
    @Operation(summary = "Update a subscription, e.g. activate a new plan")
    public ResponseEntity<SubscriptionResource> updateSubscription(@PathVariable Long subscriptionId,
                                                                   @RequestBody UpdateSubscriptionResource resource) {
        var command = UpdateSubscriptionCommandFromResourceAssembler.toCommandFromResource(subscriptionId, resource);
        return subscriptionCommandService.handle(command)
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleInvalidRequest(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleConflict(IllegalStateException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
}
