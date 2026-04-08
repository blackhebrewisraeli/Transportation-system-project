# Shift Transport System

A backend-oriented internal web application for managing employee transportation requests for shift-based operations.

## Overview

In shift-based emergency environments, some shifts require workplace-organized transportation because public transportation is unavailable or insufficient.

Today, this process is often handled manually through WhatsApp polls and ad hoc coordination.  
This project aims to replace that workflow with a structured internal system.

## Problem

The current transportation coordination process is inefficient because:

- employees must notice and respond to WhatsApp polls on time
- staff manually collect responses and prepare driver lists
- information is scattered across chat messages
- there is no structured source of truth
- transportation requests cannot be managed cleanly in advance

## MVP Goals

The current MVP focuses on four main capabilities:

- employees can log in and submit transport requests
- employees can choose pickup, drop-off, or both
- managers can view requests by date and shift
- drivers can view their assigned transport list

## Current Scope

Included in the current prototype:

- Spring Boot project structure
- PostgreSQL schema and seed data
- core entities and repositories
- initial employee / manager / driver workflow foundation

Not included yet:

- route optimization
- external calendar sync
- real SMS OTP integration
- WhatsApp bot integration
- advanced operational rules

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven
- IntelliJ IDEA
- DataGrip

## Project Structure

```text
src/main/java/com/shimon/transport/
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── service
