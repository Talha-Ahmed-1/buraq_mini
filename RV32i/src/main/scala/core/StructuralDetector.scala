package core

import chisel3._



class StructuralDetector extends Module {
  val io = IO(new Bundle {
    val rs1_sel = Input(UInt(5.W))
    val rs2_sel = Input(UInt(5.W))
    val csr_addr = Input(UInt(12.W))
   // val MEM_WB_csrAddr = Input(UInt(12.W))
    val MEM_WB_regWr = Input(UInt(1.W))
    val MEM_WB_csrWen = Input(Bool())
    val MEM_WB_REGRD = Input(UInt(5.W))
    val inst_op_in = Input(UInt(7.W))
    val fwd_rs1 = Output(UInt(1.W))
    val fwd_rs2 = Output(UInt(1.W))
    val fwd_csr = Output(Bool())
  })

  io.fwd_csr := false.B
  // additionaly checking for the lui opcode 0110111 since it does not have any rs1 or rs2 fields so no hazards can occur
  when(io.MEM_WB_regWr === 1.U && io.MEM_WB_REGRD =/= "b00000".U &&  io.MEM_WB_REGRD === io.rs1_sel && io.inst_op_in =/= "b0110111".U) {
    io.fwd_rs1 := 1.U
  } .otherwise {
    io.fwd_rs1 := 0.U
  }

  when(io.MEM_WB_regWr === 1.U && io.MEM_WB_REGRD =/= "b00000".U  && io.MEM_WB_REGRD === io.rs2_sel && io.inst_op_in =/= "b0110111".U) {
    io.fwd_rs2 := 1.U
  } .otherwise {
    io.fwd_rs2 := 0.U
  }

//  when(io.MEM_WB_regWr === 1.U && io.MEM_WB_csrWen && io.MEM_WB_csrAddr === io.csr_addr) {
//    io.fwd_csr := true.B
//  }

}