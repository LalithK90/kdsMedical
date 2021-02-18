package lk.kds_medical.asset.employee.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Designation {
  ADMIN("Admin"),
  MANAGER("Manager"),
  ACCOUNT_MANAGER("Account Manager"),
  HR_MANAGER("HR Manager"),
  CASHIER("Cashier");

  private final String designation;
}
