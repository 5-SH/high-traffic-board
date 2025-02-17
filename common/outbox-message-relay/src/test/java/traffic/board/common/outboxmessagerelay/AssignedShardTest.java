package traffic.board.common.outboxmessagerelay;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class AssignedShardTest {

    @Test
    void ofTest() {
        // given
        Long shardCount = 64L;
        List<String> appList = List.of("appId1", "appId2", "appId3");

        // when
        AssignedShard assignedShard1 = AssignedShard.of(appList.get(0), appList, shardCount);
        System.out.println("assignedShard1 = " + assignedShard1.getShards());
        AssignedShard assignedShard2 = AssignedShard.of(appList.get(1), appList, shardCount);
        System.out.println("assignedShard2 = " + assignedShard2.getShards());
        AssignedShard assignedShard3 = AssignedShard.of(appList.get(2), appList, shardCount);
        System.out.println("assignedShard3 = " + assignedShard3.getShards());
        AssignedShard assignedShard4 = AssignedShard.of("", appList, shardCount);
        System.out.println("assignedShard4 = " + assignedShard4.getShards());

        // then
        List<Long> result = Stream.of(assignedShard1.getShards(), assignedShard2.getShards(),
                        assignedShard3.getShards(), assignedShard4.getShards())
                .flatMap(List::stream)
                .toList();

        Assertions.assertThat(result).hasSize(shardCount.intValue());

        for (int i = 0; i < 64; i++) {
            Assertions.assertThat(result.get(i)).isEqualTo(i);
        }

        Assertions.assertThat(assignedShard4.getShards()).isEmpty();
    }

}