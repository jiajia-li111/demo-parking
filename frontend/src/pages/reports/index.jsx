import { useEffect, useState } from "react";
import { Button, Card, DatePicker, InputNumber, Space, Statistic, Table, Typography, message } from "antd";
import dayjs from "dayjs";
import { getCardExpiryAlerts, getDailyStats, getMonthlyStats } from "../../api/reports";

const { Title } = Typography;

export default function ReportsPage() {
  const [daily, setDaily] = useState(null);
  const [monthly, setMonthly] = useState(null);
  const [alerts, setAlerts] = useState([]);
  const [loadingDaily, setLoadingDaily] = useState(false);
  const [loadingMonthly, setLoadingMonthly] = useState(false);
  const [loadingAlerts, setLoadingAlerts] = useState(false);
  const [days, setDays] = useState(7);
  const [selectedDate, setSelectedDate] = useState(dayjs());
  const [selectedMonth, setSelectedMonth] = useState(dayjs());

  const loadDaily = async (dateValue = selectedDate) => {
    setLoadingDaily(true);
    try {
      const res = await getDailyStats(dateValue.format("YYYY-MM-DD"));
      setDaily(res);
    } catch {
      message.error("获取日统计失败");
    } finally {
      setLoadingDaily(false);
    }
  };

  const loadMonthly = async (monthValue = selectedMonth) => {
    setLoadingMonthly(true);
    try {
      const res = await getMonthlyStats(monthValue.year(), monthValue.month() + 1);
      setMonthly(res);
    } catch {
      message.error("获取月统计失败");
    } finally {
      setLoadingMonthly(false);
    }
  };

  const loadAlerts = async (daysValue = days) => {
    setLoadingAlerts(true);
    try {
      const res = await getCardExpiryAlerts(daysValue);
      setAlerts(res || []);
    } catch {
      message.error("获取到期提醒失败");
    } finally {
      setLoadingAlerts(false);
    }
  };

  useEffect(() => {
    loadDaily();
    loadMonthly();
    loadAlerts();
  }, []);

  return (
    <Space direction="vertical" size={16} style={{ width: "100%", padding: 24 }}>
      <Card>
        <Space align="center" style={{ width: "100%", justifyContent: "space-between" }}>
          <Title level={4} style={{ margin: 0 }}>
            日常统计
          </Title>
          <DatePicker
            value={selectedDate}
            onChange={(v) => {
              const value = v || dayjs();
              setSelectedDate(value);
              loadDaily(value);
            }}
          />
        </Space>
        <Space size={32} wrap style={{ marginTop: 16 }}>
          <Statistic title="日期" value={daily?.date || "-"} />
          <Statistic title="入场车辆" value={daily?.entryCount ?? "-"} />
          <Statistic title="出场车辆" value={daily?.exitCount ?? "-"} />
          <Statistic title="当日收入(元)" value={daily?.totalRevenue ?? "-"} />
          <Statistic title="平均停车时长(分钟)" value={daily?.avgParkingMinutes ?? "-"} />
        </Space>
      </Card>

      <Card>
        <Space align="center" style={{ width: "100%", justifyContent: "space-between" }}>
          <Title level={4} style={{ margin: 0 }}>
            月度统计
          </Title>
          <DatePicker
            picker="month"
            value={selectedMonth}
            onChange={(v) => {
              const value = v || dayjs();
              setSelectedMonth(value);
              loadMonthly(value);
            }}
          />
        </Space>

        <Space size={32} wrap style={{ marginTop: 16 }}>
          <Statistic title="月份" value={monthly?.month || "-"} />
          <Statistic title="月均日入场" value={monthly?.avgDailyEntry ?? "-"} />
          <Statistic title="月均日出场" value={monthly?.avgDailyExit ?? "-"} />
          <Statistic title="月度总收入(元)" value={monthly?.monthlyRevenue ?? "-"} />
        </Space>

        <Table
          style={{ marginTop: 16 }}
          rowKey={(row) => row.date}
          columns={[
            { title: "日期", dataIndex: "date" },
            { title: "入场数", dataIndex: "entryCount" },
            { title: "出场数", dataIndex: "exitCount" },
            { title: "收入(元)", dataIndex: "totalRevenue" },
            { title: "平均停车时长(分钟)", dataIndex: "avgParkingMinutes" },
          ]}
          dataSource={monthly?.dailyDetails || []}
          loading={loadingMonthly}
          pagination={false}
          size="small"
          bordered
        />
      </Card>

      <Card>
        <Space align="center" style={{ marginBottom: 12 }}>
          <Title level={4} style={{ margin: 0 }}>
            月卡到期提醒
          </Title>
          <Space>
            <InputNumber min={1} value={days} onChange={(v) => setDays(v || 1)} />
            <Button type="primary" onClick={() => loadAlerts(days)} loading={loadingAlerts}>
              重新加载
            </Button>
          </Space>
        </Space>

        <Table
          rowKey="cardId"
          columns={[
            { title: "月卡ID", dataIndex: "cardId" },
            { title: "车辆ID", dataIndex: "vehicleId" },
            { title: "车牌号", dataIndex: "licensePlate" },
            {
              title: "到期时间",
              dataIndex: "endDate",
              render: (v) => (v ? dayjs(v).format("YYYY-MM-DD HH:mm:ss") : "-"),
            },
            { title: "剩余天数", dataIndex: "remainingDays" },
          ]}
          dataSource={alerts}
          loading={loadingAlerts}
          pagination={false}
          bordered
        />
      </Card>
    </Space>
  );
}
