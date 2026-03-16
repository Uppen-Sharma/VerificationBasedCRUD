// Custom JavaScript for CRUD Store

document.addEventListener("DOMContentLoaded", () => {
  // Initialize tooltips
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  var tooltipList = tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))

  // Cart quantity buttons
  const quantityInputs = document.querySelectorAll(".quantity-input")
  if (quantityInputs) {
    quantityInputs.forEach((input) => {
      const minusBtn = input.previousElementSibling
      const plusBtn = input.nextElementSibling

      if (minusBtn && minusBtn.classList.contains("quantity-minus")) {
        minusBtn.addEventListener("click", () => {
          if (input.value > 1) {
            input.value = Number.parseInt(input.value) - 1
          }
        })
      }

      if (plusBtn && plusBtn.classList.contains("quantity-plus")) {
        plusBtn.addEventListener("click", () => {
          const max = input.getAttribute("max")
          if (!max || Number.parseInt(input.value) < Number.parseInt(max)) {
            input.value = Number.parseInt(input.value) + 1
          }
        })
      }
    })
  }

  // OTP input formatting
  const otpInput = document.getElementById("otp")
  if (otpInput) {
    otpInput.addEventListener("input", function () {
      this.value = this.value.replace(/[^0-9]/g, "").substring(0, 6)
    })
  }

  // Auto-submit form when file input changes
  const autoSubmitFileInputs = document.querySelectorAll(".auto-submit-file")
  if (autoSubmitFileInputs) {
    autoSubmitFileInputs.forEach((input) => {
      input.addEventListener("change", function () {
        if (this.files.length > 0) {
          this.closest("form").submit()
        }
      })
    })
  }

  // Product image preview
  const productImageInput = document.getElementById("image")
  const imagePreview = document.getElementById("image-preview")

  if (productImageInput && imagePreview) {
    productImageInput.addEventListener("change", function () {
      if (this.files && this.files[0]) {
        const reader = new FileReader()
        reader.onload = (e) => {
          imagePreview.src = e.target.result
          imagePreview.style.display = "block"
        }
        reader.readAsDataURL(this.files[0])
      }
    })
  }
})
